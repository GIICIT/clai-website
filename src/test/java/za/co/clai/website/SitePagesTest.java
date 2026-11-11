package za.co.clai.website;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import za.co.clai.website.content.Treatments;
import za.co.clai.website.seo.PageMeta;
import za.co.clai.website.seo.SitePages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Renders every public page (from content/pages.yml) and checks the things that have broken before or that SEO
 * depends on. Runs in every Maven build, including Jenkins' `clean install`, so a broken page stops the release.
 *
 * Per page: HTTP 200, no template error, exactly one h1, unique title and meta description, canonical = the page's
 * own https URL, valid JSON-LD, and every local image/CSS/JS it references loads.
 * Site-wide: every page template/route has a pages.yml entry, every treatments.yml entry is used, the sitemap lists every page, unknown URLs
 * return a real 404 page, and the permanent redirects work.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SitePagesTest {

    /** Routes that are not content pages (redirects, generated files, form endpoints, error handling). */
    private static final Set<String> NON_PAGE_ROUTES = Set.of("/home", "/acne-and-acne-scaring", "/sitemap.xml", "/email", "/error");

    /** Templates that are deliberately not pages (none since skin-treatments.html was deleted in Phase 3). */
    private static final Set<String> UNLISTED_TEMPLATES = Set.of();

    private static final Pattern ITEMS = Pattern.compile("items='([^']*)'");

    /** Known, accepted gaps: references that are broken today on purpose (see the clai-website skill). */
    private static final Set<String> KNOWN_MISSING_ASSETS = Set.of("images/blog/01.jpg");

    private static final Pattern H1 = Pattern.compile("<h1\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern TITLE = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
    private static final Pattern DESCRIPTION = Pattern.compile("<meta name=\"description\" content=\"([^\"]*)\"");
    private static final Pattern CANONICAL = Pattern.compile("<link rel=\"canonical\" href=\"([^\"]*)\"");
    private static final Pattern JSON_LD = Pattern.compile("<script type=\"application/ld\\+json\">(.*?)</script>", Pattern.DOTALL);
    /** Text sitting loose between scripts/blocks, e.g. a typo after a closing tag ("</th:block>F" once rendered an "F"). */
    private static final Pattern STRAY_TEXT = Pattern.compile("</script>\\s*([^<\\s][^<]*)");
    private static final Pattern ASSET = Pattern.compile("(?:src|href)=\"(/?(?:img|css|js|docs)/[^\"#?]+)\"|url\\((/?img/[^)]+)\\)");

    @Autowired private MockMvc mvc;
    @Autowired private SitePages sitePages;
    @Autowired private RequestMappingHandlerMapping handlerMapping;
    @Autowired private Treatments treatments;

    private final ObjectMapper json = new ObjectMapper();

    /** Site-wide, independent of the per-page checks so one failing page can't hide a duplicate. */
    @Test
    void titlesAndDescriptionsAreUnique() throws Exception {
        Map<String, List<String>> titles = new HashMap<>();
        Map<String, List<String>> descriptions = new HashMap<>();
        for (PageMeta page : sitePages.all()) {
            String html = render(page.path(), 200);
            titles.computeIfAbsent(first(TITLE, html), k -> new ArrayList<>()).add(page.path());
            descriptions.computeIfAbsent(first(DESCRIPTION, html), k -> new ArrayList<>()).add(page.path());
        }
        titles.values().removeIf(paths -> paths.size() < 2);
        descriptions.values().removeIf(paths -> paths.size() < 2);
        assertThat(titles).as("pages sharing a <title>").isEmpty();
        assertThat(descriptions).as("pages sharing a meta description").isEmpty();
    }

    @TestFactory
    Stream<DynamicTest> everyPageRendersCorrectly() {
        return sitePages.all().stream().map(page -> DynamicTest.dynamicTest(page.path(), () -> {
            String html = render(page.path(), 200);

            assertThat(html).as("template error on %s", page.path())
                    .doesNotContain("TemplateProcessingException").doesNotContain("Whitelabel Error");
            assertThat(all(STRAY_TEXT, html)).as("stray text after a </script> on %s", page.path()).isEmpty();
            assertThat(count(H1, html)).as("number of <h1> on %s", page.path()).isEqualTo(1);

            assertThat(first(TITLE, html)).as("title on %s", page.path()).isNotBlank().hasSizeLessThanOrEqualTo(70);
            assertThat(first(DESCRIPTION, html)).as("meta description on %s", page.path()).isNotBlank().hasSizeLessThanOrEqualTo(170);

            assertThat(first(CANONICAL, html)).as("canonical on %s", page.path()).isEqualTo(sitePages.absolute(page.path()));

            List<String> blocks = all(JSON_LD, html);
            assertThat(blocks).as("structured data on %s", page.path()).isNotEmpty();
            for (String block : blocks) {
                assertThat(json.readTree(block).has("@type")).as("JSON-LD @type on %s", page.path()).isTrue();
            }

            for (String asset : assets(html)) {
                if (KNOWN_MISSING_ASSETS.stream().anyMatch(asset::endsWith)) continue;
                String url = asset.startsWith("/") ? asset : "/" + asset;
                int status = mvc.perform(get(url)).andReturn().getResponse().getStatus();
                assertThat(status).as("%s referenced by %s", url, page.path()).isEqualTo(200);
            }
        }));
    }

    /**
     * Pages are served by PageController from pages.yml, so check the other direction too: every page template
     * is listed (an unlisted template is unreachable), and no explicit GET route bypasses pages.yml.
     */
    @Test
    void everyPageTemplateAndRouteIsListedInPagesYml() throws Exception {
        Set<String> listed = new TreeSet<>();
        sitePages.all().forEach(p -> listed.add(p.path()));

        Set<String> templates = new TreeSet<>();
        try (Stream<Path> files = Files.list(Path.of("src/main/resources/templates"))) {
            files.map(f -> f.getFileName().toString()).filter(n -> n.endsWith(".html"))
                    .map(n -> n.equals("index.html") ? "/" : "/" + n.substring(0, n.length() - 5))
                    .filter(path -> !UNLISTED_TEMPLATES.contains(path))
                    .forEach(templates::add);
        }
        assertThat(templates).as("page templates without a content/pages.yml entry").isSubsetOf(listed);

        Set<String> routes = new TreeSet<>();
        for (RequestMappingInfo info : handlerMapping.getHandlerMethods().keySet()) {
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            if (!methods.isEmpty() && !methods.contains(RequestMethod.GET)) continue;
            info.getPatternValues().stream().filter(r -> !r.contains("{")).forEach(routes::add);
        }
        routes.removeAll(NON_PAGE_ROUTES);
        assertThat(routes).as("explicit routes without a content/pages.yml entry").isSubsetOf(listed);
    }

    /** Every id a page lists exists (else the page fails above), and every entry in treatments.yml is used. */
    @Test
    void everyTreatmentIsUsed() throws Exception {
        Set<String> used = new TreeSet<>();
        try (Stream<Path> files = Files.walk(Path.of("src/main/resources/templates"))) {
            for (Path f : files.filter(x -> x.toString().endsWith(".html")).toList()) {
                String withoutComments = Files.readString(f).replaceAll("(?s)<!--.*?-->", ""); // docs show example ids
                Matcher m = ITEMS.matcher(withoutComments);
                while (m.find()) used.addAll(treatments.list(m.group(1)));
            }
        }
        treatments.all().forEach(t -> used.addAll(t.children()));
        Set<String> defined = new TreeSet<>();
        treatments.all().forEach(t -> defined.add(t.id()));
        org.assertj.core.api.SoftAssertions.assertSoftly(soft -> { // report both lists, not just the first
            soft.assertThat(used).as("ids used by pages but missing from content/treatments.yml").isSubsetOf(defined);
            soft.assertThat(defined).as("content/treatments.yml entries no page uses").isSubsetOf(used);
        });
    }

    /**
     * Structured data must match what visitors see: every offer price in the JSON-LD appears in the page's text,
     * and every treatment page with prices in its cards publishes an offer catalogue.
     */
    @TestFactory
    Stream<DynamicTest> offerPricesMatchTheVisiblePage() {
        return sitePages.all().stream().filter(PageMeta::isTreatment).map(page -> DynamicTest.dynamicTest(page.path(), () -> {
            String html = render(page.path(), 200);
            String digits = html.replaceAll("(?s)<script.*?</script>", " ").replaceAll("<[^>]+>", " ")
                    .replaceAll("(?<=\\d)[ ,](?=\\d{3}\\b)", "");   // "R 15 990" / "R7,900" -> 15990 / 7900
            List<Integer> prices = new ArrayList<>();
            for (String block : all(JSON_LD, html)) {
                com.fasterxml.jackson.databind.JsonNode node = json.readTree(block);
                if (!node.has("hasOfferCatalog")) continue;
                for (com.fasterxml.jackson.databind.JsonNode offer : node.get("hasOfferCatalog").get("itemListElement")) {
                    assertThat(offer.at("/itemOffered/name").asText()).as("offer name on %s", page.path()).isNotBlank();
                    for (String f : List.of("/price", "/priceSpecification/minPrice", "/priceSpecification/maxPrice")) {
                        if (!offer.at(f).isMissingNode()) prices.add(offer.at(f).asInt());
                    }
                }
            }
            boolean cardsShowPrices = html.matches("(?s).*class=\"card-body\".*\\bR\\s?\\d.*");
            if (cardsShowPrices) assertThat(prices).as("offer catalogue on %s", page.path()).isNotEmpty();
            for (int price : prices) {
                assertThat(digits).as("price %s from the JSON-LD shown on %s", price, page.path()).containsPattern("R\\s?" + price + "\\b");
            }
        }));
    }

    @Test
    void sitemapListsEveryPage() throws Exception {
        MvcResult result = mvc.perform(get("/sitemap.xml")).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).startsWith(MediaType.APPLICATION_XML_VALUE);
        String xml = result.getResponse().getContentAsString();
        for (PageMeta page : sitePages.all()) {
            assertThat(xml).as("sitemap entry for %s", page.path()).contains("<loc>" + sitePages.absolute(page.path()) + "</loc>");
        }
    }

    @Test
    void unknownUrlsReturnARealNotFoundPage() throws Exception {
        // 1. An unknown URL answers 404 (not a redirect to the home page).
        render("/this-page-does-not-exist", 404);
        // 2. The page shown for it. MockMvc has no servlet container, so it doesn't forward to /error by itself;
        //    do the forward the way Tomcat does (request attributes set by the container).
        MvcResult result = mvc.perform(get("/error").accept(MediaType.TEXT_HTML)
                .requestAttr(jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE, 404)
                .requestAttr(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI, "/this-page-does-not-exist")).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        String html = result.getResponse().getContentAsString();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        assertThat(html).contains("Page not found");
        assertThat(count(H1, html)).isEqualTo(1);
        assertThat(html).as("404 page must not have a canonical").doesNotContain("rel=\"canonical\"");
    }

    @Test
    void oldUrlsRedirectPermanently() throws Exception {
        assertRedirect("/home", "/");
        assertRedirect("/acne-and-acne-scaring", "/acne-and-acne-scarring");
    }

    @Test
    void templatesNotInPagesYmlAreNotServed() throws Exception {
        render("/skin-treatments", 404);
    }

    // ------------------------------------------------------------------ helpers

    private String render(String path, int expectedStatus) throws Exception {
        MvcResult result = mvc.perform(get(path).accept(MediaType.TEXT_HTML)).andReturn();
        assertThat(result.getResponse().getStatus()).as("HTTP status of %s", path).isEqualTo(expectedStatus);
        result.getResponse().setCharacterEncoding("UTF-8");
        return result.getResponse().getContentAsString();
    }

    private void assertRedirect(String from, String to) throws Exception {
        MvcResult result = mvc.perform(get(from)).andReturn();
        assertThat(result.getResponse().getStatus()).as("status of %s", from).isEqualTo(301);
        assertThat(result.getResponse().getHeader(HttpHeaders.LOCATION)).as("target of %s", from).endsWith(to);
    }

    private static int count(Pattern p, String s) {
        Matcher m = p.matcher(s);
        int n = 0;
        while (m.find()) n++;
        return n;
    }

    private static String first(Pattern p, String s) {
        Matcher m = p.matcher(s);
        return m.find() ? m.group(1).trim() : null;
    }

    private static List<String> all(Pattern p, String s) {
        List<String> out = new ArrayList<>();
        Matcher m = p.matcher(s);
        while (m.find()) out.add(m.group(1));
        return out;
    }

    private static Set<String> assets(String html) {
        Set<String> out = new LinkedHashSet<>();
        Matcher m = ASSET.matcher(html);
        while (m.find()) out.add(m.group(1) != null ? m.group(1) : m.group(2));
        return out;
    }
}

package za.co.clai.website.content;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads content/treatments.yml once at startup: every accordion card's title and text, written once and
 * referenced from pages by id. Templates use it as {@code @treatments} (see fragments/accordion.html).
 * Fails fast on startup if the file is broken, an entry lacks a title/body, or a nested child doesn't exist.
 */
@Component("treatments")
public class Treatments {

    private static final String[] WORDS = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
            "Nineteen", "Twenty", "TwentyOne", "TwentyTwo", "TwentyThree", "TwentyFour", "TwentyFive"};

    private final Map<String, Treatment> byId;

    @SuppressWarnings("unchecked")
    public Treatments() throws IOException {
        Map<String, Object> root;
        try (InputStream in = new ClassPathResource("content/treatments.yml").getInputStream()) {
            root = new Yaml().load(in);
        }
        Map<String, Treatment> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : root.entrySet()) {
            String id = e.getKey();
            Map<String, Object> t = (Map<String, Object>) e.getValue();
            String title = required(t, "title", id);
            String body = required(t, "body", id).strip();
            List<String> children = t.get("children") == null ? List.of()
                    : List.copyOf((Collection<String>) t.get("children"));
            map.put(id, new Treatment(id, title, body, children, TreatmentPrice.parse(body), summary(body)));
        }
        for (Treatment t : map.values()) {
            for (String child : t.children()) {
                if (!map.containsKey(child)) {
                    throw new IllegalStateException("content/treatments.yml: " + t.id() + " has unknown child " + child);
                }
            }
        }
        this.byId = Collections.unmodifiableMap(map);
    }

    /** The treatment with this id; an unknown id is a template mistake, so it fails the page (and the tests). */
    public Treatment get(String id) {
        Treatment t = byId.get(id);
        if (t == null) {
            throw new IllegalArgumentException("Unknown treatment id '" + id + "' (not in content/treatments.yml)");
        }
        return t;
    }

    public Collection<Treatment> all() {
        return byId.values();
    }

    /** Ids as a list: accepts a list, or a string of ids separated by commas and/or whitespace. */
    public List<String> list(Object ids) {
        if (ids == null) {
            return List.of();
        }
        if (ids instanceof Collection<?> c) {
            return c.stream().map(Object::toString).toList();
        }
        List<String> out = new ArrayList<>(Arrays.asList(ids.toString().trim().split("[,\\s]+")));
        out.removeIf(String::isEmpty);
        return out;
    }

    /** Card number word used in ids: 1 -> "One". */
    public String word(int n) {
        if (n < 1 || n > WORDS.length) {
            throw new IllegalArgumentException("accordion has more than " + WORDS.length + " cards");
        }
        return WORDS[n - 1];
    }

    /**
     * schema.org data for the priced treatments in a section, as the clinic's offer catalogue:
     * {"@type": "MedicalBusiness", "@id": business, "hasOfferCatalog": {OfferCatalog of Offer -> Service}}.
     * Rendered as JSON-LD by fragments/sections; the @id merges it into the site-wide MedicalBusiness.
     * Returns null when none of the cards states a price (e.g. "Types of Acne").
     */
    public Map<String, Object> offerCatalog(Object ids, String name, String pageUrl, String businessId) {
        List<Object> offers = new ArrayList<>();
        for (String id : list(ids)) {
            Treatment t = get(id);
            if (t.price() == null) {
                continue;
            }
            Map<String, Object> service = new LinkedHashMap<>();
            service.put("@type", "Service");
            service.put("name", t.title());
            if (!t.summary().isEmpty()) {
                service.put("description", t.summary());
            }
            service.put("url", pageUrl);
            service.put("provider", Map.of("@id", businessId));
            Map<String, Object> offer = new LinkedHashMap<>();
            offer.put("@type", "Offer");
            offer.put("itemOffered", service);
            TreatmentPrice p = t.price();
            if (p.exact() != null) {
                offer.put("price", p.exact());
                offer.put("priceCurrency", "ZAR");
            } else {
                Map<String, Object> spec = new LinkedHashMap<>();
                spec.put("@type", "PriceSpecification");
                spec.put("priceCurrency", "ZAR");
                spec.put("minPrice", p.min());
                if (p.max() != null) {
                    spec.put("maxPrice", p.max());
                }
                if (p.unit() != null) {
                    spec.put("unitText", p.unit());
                }
                offer.put("priceSpecification", spec);
            }
            offers.add(offer);
        }
        if (offers.isEmpty()) {
            return null;
        }
        Map<String, Object> catalog = new LinkedHashMap<>();
        catalog.put("@type", "OfferCatalog");
        catalog.put("name", name);
        catalog.put("url", pageUrl);
        catalog.put("itemListElement", offers);
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("@context", "https://schema.org");
        root.put("@type", "MedicalBusiness");
        root.put("@id", businessId);
        root.put("hasOfferCatalog", catalog);
        return root;
    }

    /** First paragraph (up to the first blank line break or list) as plain text, cut at a word near 300 chars. */
    static String summary(String bodyHtml) {
        String first = bodyHtml.split("<br>\\s*<br>|<ul", 2)[0];
        String text = first.replaceAll("<[^>]+>", " ")
                .replace("&amp;", "&").replace("&rsquo;", "’").replace("&nbsp;", " ")
                .replaceAll("&[a-z]+;", " ").replaceAll("\\s+", " ").trim();
        if (text.length() <= 300) {
            return text;
        }
        int cut = text.lastIndexOf(' ', 297);
        return text.substring(0, cut > 0 ? cut : 297) + "…";
    }

    private static String required(Map<String, Object> m, String key, String id) {
        Object v = m.get(key);
        if (v == null || v.toString().isBlank()) {
            throw new IllegalStateException("content/treatments.yml: '" + key + "' missing for " + id);
        }
        return v.toString();
    }
}

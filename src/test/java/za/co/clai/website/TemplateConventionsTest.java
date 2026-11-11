package za.co.clai.website;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Static checks on every Thymeleaf template, for mistakes that broke or nearly broke the site before (Nov 2026).
 * Fast (no Spring context) and precise: each failure names the file and line.
 *
 *  - fragment includes must be wrapped, th:insert="~{fragments/x :: y}": the unwrapped form is deprecated in
 *    Thymeleaf 3.1 (a WARN per request) and will be removed;
 *  - no Spring bean calls (${@bean...}) inside th:replace/th:insert: Thymeleaf 3.1.3+ refuses them ("Instantiation of new
 *    objects and access to static classes or parameters is forbidden"), which broke every treatment page once;
 *  - no fragment named after an HTML tag (":: body" also selected the <body> element: infinite recursion), except
 *    when declared on that very tag (<head th:fragment="head(type)">);
 *  - no "[#" inside th:inline="javascript" blocks (starts a text-template element: 500 on every page);
 *  - no copied inline title styles (use the .clai-title / .clai-text classes).
 */
class TemplateConventionsTest {

    private static final Path TEMPLATES = Path.of("src/main/resources/templates");

    private static final Pattern COMMENT = Pattern.compile("(?s)<!--.*?-->");
    private static final Pattern UNWRAPPED_INCLUDE = Pattern.compile("th:(insert|replace|include)=\"(?![~$])[^\"]*\"");
    private static final Pattern BEAN_IN_INCLUDE = Pattern.compile("th:(insert|replace)=\"[^\"]*\\$\\{@[^\"]*\"");
    /** group 1 = the element carrying th:fragment, group 2 = the fragment name. */
    private static final Pattern FRAGMENT_NAME = Pattern.compile("<([A-Za-z][A-Za-z0-9:-]*)\\b[^>]*?th:fragment=\"([A-Za-z0-9_-]+)");
    private static final Pattern JS_INLINE_BLOCK = Pattern.compile("(?s)<script[^>]*th:inline=\"javascript\"[^>]*>(.*?)</script>");
    private static final Pattern INLINE_TITLE_STYLE = Pattern.compile("class=\"title\"[^>]*style=\"[^\"]*font-size:\\s*\\d+px\\s*!important");
    private static final Set<String> HTML_TAGS = Set.of("html", "head", "body", "title", "meta", "link", "script", "style",
            "header", "footer", "main", "nav", "section", "article", "aside", "div", "span", "p", "a", "img", "ul", "ol",
            "li", "table", "tr", "td", "th", "form", "input", "button", "label", "select", "option", "textarea", "h1", "h2",
            "h3", "h4", "h5", "h6", "br", "hr", "b", "i", "em", "strong", "small", "iframe", "picture", "source", "svg");

    @TestFactory
    Stream<DynamicTest> everyTemplateFollowsTheConventions() throws IOException {
        List<Path> files;
        try (Stream<Path> s = Files.walk(TEMPLATES)) {
            files = s.filter(f -> f.toString().endsWith(".html")).sorted().toList();
        }
        assertThat(files).as("templates found").isNotEmpty();
        return files.stream().map(f -> DynamicTest.dynamicTest(TEMPLATES.relativize(f).toString(), () -> {
            String raw = Files.readString(f);
            String src = blankComments(raw);   // keep line numbers, ignore examples in documentation comments
            List<String> problems = new ArrayList<>();
            find(src, UNWRAPPED_INCLUDE, "unwrapped fragment include, write th:...=\"~{...}\"", problems);
            find(src, BEAN_IN_INCLUDE, "bean call inside th:replace/th:insert, compute it in th:with first", problems);
            find(src, INLINE_TITLE_STYLE, "inline title style, use class clai-title clai-title-lg/md", problems);
            Matcher m = FRAGMENT_NAME.matcher(src);
            while (m.find()) {
                String element = m.group(1).toLowerCase(), name = m.group(2).toLowerCase();
                // allowed only on the element of the same name (<head th:fragment="head(...)">): then the selector can
                // only ever match that one element; anywhere else ":: name" may also select a real <name> element
                if (HTML_TAGS.contains(name) && !name.equals(element)) {
                    problems.add(line(src, m.start()) + ": fragment named after an HTML tag: " + m.group(2) + " (on <" + element + ">)");
                }
            }
            Matcher js = JS_INLINE_BLOCK.matcher(src);
            while (js.find()) {
                int at = js.group(1).indexOf("[#");
                if (at >= 0) problems.add(line(src, js.start(1) + at) + ": \"[#\" inside th:inline=\"javascript\", compute it in Java");
            }
            assertThat(problems).as("template convention problems in %s", f.getFileName()).isEmpty();
        }));
    }

    private static void find(String src, Pattern p, String what, List<String> out) {
        Matcher m = p.matcher(src);
        while (m.find()) {
            out.add(line(src, m.start()) + ": " + what + ": " + m.group().substring(0, Math.min(90, m.group().length())));
        }
    }

    private static String blankComments(String s) {
        Matcher m = COMMENT.matcher(s);
        StringBuilder b = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(b, Matcher.quoteReplacement(m.group().replaceAll("[^\\n]", " ")));
        }
        m.appendTail(b);
        return b.toString();
    }

    private static String line(String s, int index) {
        return "line " + (s.substring(0, index).chars().filter(c -> c == '\n').count() + 1);
    }
}

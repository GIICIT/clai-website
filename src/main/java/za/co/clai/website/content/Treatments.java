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
            map.put(id, new Treatment(id, title, body, children));
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

    private static String required(Map<String, Object> m, String key, String id) {
        Object v = m.get(key);
        if (v == null || v.toString().isBlank()) {
            throw new IllegalStateException("content/treatments.yml: '" + key + "' missing for " + id);
        }
        return v.toString();
    }
}

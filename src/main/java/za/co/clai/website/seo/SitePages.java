package za.co.clai.website.seo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads content/pages.yml once at startup: the list of public pages and their SEO data.
 * Fails fast on startup if the file is missing or a page lacks a required field.
 */
@Component
public class SitePages {

    private final String baseUrl;
    private final String siteName;
    private final String defaultImage;
    private final List<PageMeta> pages;
    private final Map<String, PageMeta> byPath;

    @SuppressWarnings("unchecked")
    public SitePages() throws IOException {
        Map<String, Object> root;
        try (InputStream in = new ClassPathResource("content/pages.yml").getInputStream()) {
            root = new Yaml().load(in);
        }
        Map<String, Object> site = (Map<String, Object>) root.get("site");
        this.baseUrl = stripTrailingSlash(required(site, "baseUrl", "site"));
        this.siteName = required(site, "name", "site");
        this.defaultImage = required(site, "defaultImage", "site");

        List<PageMeta> list = new ArrayList<>();
        Map<String, PageMeta> map = new LinkedHashMap<>();
        for (Map<String, Object> p : (List<Map<String, Object>>) root.get("pages")) {
            String path = required(p, "path", "page");
            PageMeta meta = new PageMeta(path, required(p, "name", path), required(p, "title", path),
                    required(p, "description", path), str(p.get("image")), required(p, "lastmod", path),
                    str(p.get("type")), str(p.get("datePublished")), str(p.get("author")));
            if (map.put(path, meta) != null) {
                throw new IllegalStateException("content/pages.yml: duplicate path " + path);
            }
            list.add(meta);
        }
        this.pages = Collections.unmodifiableList(list);
        this.byPath = Collections.unmodifiableMap(map);
    }

    /** Page for a request path, or null (e.g. error pages). */
    public PageMeta find(String path) {
        return byPath.get(path);
    }

    public List<PageMeta> all() {
        return pages;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    /** Absolute URL for a site path, e.g. "/toxin" -> "https://clai.co.za/toxin". */
    public String absolute(String path) {
        return baseUrl + path;
    }

    /** Absolute sharing image for a page (its own image or the site default). */
    public String imageFor(PageMeta page) {
        return absolute(page != null && page.image() != null ? page.image() : defaultImage);
    }

    private static String required(Map<String, Object> m, String key, String where) {
        String v = str(m.get(key));
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("content/pages.yml: '" + key + "' missing for " + where);
        }
        return v;
    }

    private static String str(Object o) {
        if (o instanceof java.util.Date d) { // YAML turns unquoted 2026-11-11 into a Date
            return d.toInstant().atZone(java.time.ZoneOffset.UTC).toLocalDate().toString();
        }
        return o == null ? null : o.toString();
    }

    private static String stripTrailingSlash(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}

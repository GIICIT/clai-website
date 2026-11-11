package za.co.clai.website.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import za.co.clai.website.seo.PageMeta;
import za.co.clai.website.seo.SitePages;

/**
 * /sitemap.xml generated from content/pages.yml, so every page listed there (and only those)
 * appears with its canonical https URL and lastmod. Replaces the hand-maintained static file.
 */
@RestController
public class SitemapController {

    private final SitePages sitePages;

    public SitemapController(SitePages sitePages) {
        this.sitePages = sitePages;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        for (PageMeta page : sitePages.all()) {
            xml.append("  <url>\n")
               .append("    <loc>").append(HtmlUtils.htmlEscape(sitePages.absolute(page.path()))).append("</loc>\n")
               .append("    <lastmod>").append(page.lastmod()).append("</lastmod>\n")
               .append("  </url>\n");
        }
        xml.append("</urlset>\n");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xml.toString());
    }
}

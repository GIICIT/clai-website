package za.co.clai.website.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import za.co.clai.website.seo.PageMeta;
import za.co.clai.website.seo.SitePages;

/**
 * Serves every content page listed in content/pages.yml: /{slug} renders templates/{slug}.html.
 * Adding a page = a template + a pages.yml entry; no controller code. Anything not in pages.yml is a 404.
 * More specific mappings win over this one: "/" (HomeController), "/contact-us" (needs the form model),
 * the 301 redirects, /sitemap.xml and /error.
 * The navbar highlights the current page from the `page` model attribute (GlobalModelAdvice).
 */
@Controller
public class PageController {

    private final SitePages sitePages;

    public PageController(SitePages sitePages) {
        this.sitePages = sitePages;
    }

    @GetMapping("/{slug:[a-z0-9-]+}")
    public String page(@PathVariable String slug) {
        PageMeta page = sitePages.find("/" + slug);
        if (page == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return slug;
    }
}

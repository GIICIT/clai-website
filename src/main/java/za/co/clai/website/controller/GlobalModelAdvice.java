package za.co.clai.website.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import za.co.clai.website.model.SocialMediaLinksModel;
import za.co.clai.website.seo.PageMeta;
import za.co.clai.website.seo.SitePages;

/**
 * Model attributes every page needs, added once for all controllers (including Spring Boot's
 * error controller, so the 404 page renders the full footer):
 *   facebook, instagram - footer social links
 *   socialLinks         - the real (http) social URLs, for structured data "sameAs"
 *   site                - SitePages (base URL, all pages) for canonical URLs and structured data
 *   page                - this page's SEO data from content/pages.yml, or null (error pages)
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final SocialMediaLinksModel socialMediaLinksModel;
    private final SitePages sitePages;

    public GlobalModelAdvice(SocialMediaLinksModel socialMediaLinksModel, SitePages sitePages) {
        this.socialMediaLinksModel = socialMediaLinksModel;
        this.sitePages = sitePages;
    }

    /** Real social profile URLs for structured data "sameAs" (placeholders like "#" are skipped). */
    @ModelAttribute("socialLinks")
    public java.util.List<String> socialLinks() {
        return java.util.stream.Stream.of(socialMediaLinksModel.getInstagram(), socialMediaLinksModel.getFacebook())
                .filter(url -> url != null && url.startsWith("http"))
                .toList();
    }

    @ModelAttribute("site")
    public SitePages site() {
        return sitePages;
    }

    @ModelAttribute("page")
    public PageMeta page(HttpServletRequest request) {
        return sitePages.find(request.getRequestURI());
    }

    @ModelAttribute("facebook")
    public String facebook() {
        return socialMediaLinksModel.getFacebook();
    }

    @ModelAttribute("instagram")
    public String instagram() {
        return socialMediaLinksModel.getInstagram();
    }
}

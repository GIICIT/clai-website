package za.co.clai.website.seo;

/**
 * SEO data for one public page, loaded from content/pages.yml.
 *
 * @param path          URL path, e.g. "/toxin" (must match the controller mapping)
 * @param name          short name for breadcrumbs
 * @param title         &lt;title&gt; (and og:title)
 * @param description   meta description (and og:description)
 * @param image         sharing image path (site-relative) or null for the site default
 * @param lastmod       last content change, YYYY-MM-DD (sitemap)
 * @param type          "treatment" (Treatments menu), "blog" (Blog menu), or null
 * @param datePublished blog posts: YYYY-MM-DD
 * @param author        blog posts: author name
 */
public record PageMeta(String path, String name, String title, String description, String image,
                       String lastmod, String type, String datePublished, String author) {

    public boolean isBlog() {
        return "blog".equals(type);
    }

    public boolean isTreatment() {
        return "treatment".equals(type);
    }

    public boolean isHome() {
        return "/".equals(path);
    }
}

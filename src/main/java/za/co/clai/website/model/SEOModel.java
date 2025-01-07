package za.co.clai.website.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SEOModel {
    @Value("${title}")
    private String pageTitle;

    @Value("${meta-description}")
    private String metaDescription;

    public String getPageTitle() {
        return pageTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }
}
package za.co.clai.website.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocialMediaLinksModel {
    @Value("${facebook}")
    private String facebook;

    @Value("${instagram}")
    private String instagram;

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

}

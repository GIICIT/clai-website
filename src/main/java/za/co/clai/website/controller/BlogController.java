package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class BlogController {
    final SocialMediaLinksModel socialMediaLinksModel;

    public BlogController(SocialMediaLinksModel socialMediaLinksModel) {
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

    @GetMapping(value = {"/blog"})
    public String blogPage(Model model) {
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        model.addAttribute("youtube", socialMediaLinksModel.getYoutube());
        return "blog";
    }
}

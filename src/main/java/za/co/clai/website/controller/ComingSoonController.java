package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class ComingSoonController {
    final SocialMediaLinksModel socialMediaLinksModel;

    public ComingSoonController(SocialMediaLinksModel socialMediaLinksModel) {
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

    @GetMapping(value = {"/coming-soon"})
    public String blogPage(Model model) {
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "coming-soon";
    }
}

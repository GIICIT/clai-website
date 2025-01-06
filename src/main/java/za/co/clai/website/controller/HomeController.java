package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class HomeController {

    final SocialMediaLinksModel socialMediaLinksModel;

    public HomeController(SocialMediaLinksModel socialMediaLinksModel) {
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

    @GetMapping(value = {"/", "/home"})
    public String homePage(Model model) {
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "index";
    }

}

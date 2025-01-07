package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import za.co.clai.website.model.SEOModel;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class HomeController {

    final SEOModel seoModel;

    final SocialMediaLinksModel socialMediaLinksModel;

    public HomeController(SEOModel seoModel, SocialMediaLinksModel socialMediaLinksModel) {
        this.seoModel = seoModel;
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

    @GetMapping(value = {"/", "/home"})
    public String homePage(Model model) {
        model.addAttribute("seo", seoModel);
        model.addAttribute("socialMedia", socialMediaLinksModel);
        model.addAttribute("youtube", socialMediaLinksModel.getYoutube());
        return "index";
    }

}

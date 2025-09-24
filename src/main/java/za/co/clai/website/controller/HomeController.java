package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class HomeController {

    final SocialMediaLinksModel socialMediaLinksModel;

    public HomeController(SocialMediaLinksModel socialMediaLinksModel) {
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("isHomeActive", true);
        model.addAttribute("isTreatmentsActive", false);
        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", false);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "index";
    }

    @GetMapping("/home")
    public RedirectView redirectToRoot() {
        return new RedirectView("/", true);
    }

}

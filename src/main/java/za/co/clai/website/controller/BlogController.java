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

    @GetMapping(value = {"/blog-single-why-clai"})
    public String blogPageWhyClai(Model model) {
        model.addAttribute("isHomeActive", false);
        model.addAttribute("isTreatmentsActive", false);
        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", true);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "blog-single-why-clai";
    }

    @GetMapping(value = {"/winter-skin-prep"})
    public String blogPageWinterSkinPrep(Model model) {
        model.addAttribute("isHomeActive", false);
        model.addAttribute("isTreatmentsActive", false);
        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", true);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "winter-skin-prep";
    }
}

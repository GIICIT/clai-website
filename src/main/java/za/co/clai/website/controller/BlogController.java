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
        model.addAttribute("isBlogActive", true);
        model.addAttribute("isWhyClaiActive", true);
        return "blog-single-why-clai";
    }

    @GetMapping(value = {"/winter-skin-prep"})
    public String blogPageWinterSkinPrep(Model model) {
        model.addAttribute("isBlogActive", true);
        model.addAttribute("isWinterSkinPrepActive", true);
        return "winter-skin-prep";
    }
}

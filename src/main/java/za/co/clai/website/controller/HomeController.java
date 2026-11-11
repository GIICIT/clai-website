package za.co.clai.website.controller;

import org.springframework.http.HttpStatus;
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
        return "index";
    }

    /** Old duplicate URL: permanent (301) redirect so search engines keep only "/". */
    @GetMapping("/home")
    public RedirectView redirectToRoot() {
        RedirectView redirect = new RedirectView("/", true);
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirect;
    }

    /** Old misspelled URL (renamed Nov 2026): 301 so links and search rankings carry over. */
    @GetMapping("/acne-and-acne-scaring")
    public RedirectView redirectOldAcneUrl() {
        RedirectView redirect = new RedirectView("/acne-and-acne-scarring", true);
        redirect.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirect;
    }

}

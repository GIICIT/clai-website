package za.co.clai.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactUsController {
    @GetMapping(value = {"/contact-us",})
    public String contactUsPage(Model model) {
        return "contact-us";
    }
}






package za.co.clai.website.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import za.co.clai.website.model.EmailModel;
import za.co.clai.website.model.SocialMediaLinksModel;
import za.co.clai.website.service.EmailService;

@Controller
public class ContactUsController {

    final SocialMediaLinksModel socialMediaLinksModel;
    final EmailService emailService;

    public ContactUsController(SocialMediaLinksModel socialMediaLinksModel, EmailService emailService) {
        this.socialMediaLinksModel = socialMediaLinksModel;
        this.emailService = emailService;
    }

    @GetMapping(value = {"/contact-us"})
    public String contactUsPage(Model model) {
        model.addAttribute("emailModel", new EmailModel());
        return "contact-us";
    }

    @PostMapping("/email")
    public String email(
            @Valid @ModelAttribute("emailModel") EmailModel emailModel,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "contact-us";
        }

        emailService.sendSimpleMessage(emailModel);
        return "redirect:/contact-us";
    }
}






package za.co.clai.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HttpErrorController {

    @GetMapping("/404")
    public String homePage(Model model) {
        return "404";
    }

}

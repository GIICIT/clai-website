package za.co.clai.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import za.co.clai.website.model.SocialMediaLinksModel;

@Controller
public class TreatmentsController {

    final SocialMediaLinksModel socialMediaLinksModel;

    public TreatmentsController(SocialMediaLinksModel socialMediaLinksModel) {
        this.socialMediaLinksModel = socialMediaLinksModel;
    }

//    @GetMapping(value = { "/skin-treatments"})
//    public String treatmentsPage(Model model) {
//        model.addAttribute("isHomeActive", false);
//
//        model.addAttribute("isTreatmentsActive", true);
//        model.addAttribute("isSkinTreatmentsActive", true);
//        model.addAttribute("isIvyTherapyActive", false);
//        model.addAttribute("isToxinActive", false);
//
//        model.addAttribute("isPriceListActive", false);
//
//        model.addAttribute("isBlogActive", false);
//        model.addAttribute("isWhyClaiActive", false);
//        model.addAttribute("isWinterSkinPrepActive", false);
//
//        model.addAttribute("isContactUsActive", false);
//        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
//        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
//        return "skin-treatments";
//    }

    @GetMapping(value = { "/ivy-therapy"})
    public String ivyTherapyPage(Model model) {
        model.addAttribute("isHomeActive", false);

        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIvyTherapyActive", true);
        model.addAttribute("isToxinActive", false);
        model.addAttribute("isDermalFillerActive", false);
        model.addAttribute("isBiostimulatorsActive", false);

        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", false);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "ivy-therapy";
    }

    @GetMapping(value = { "/toxin"})
    public String toxinPage(Model model) {
        model.addAttribute("isHomeActive", false);

        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIvyTherapyActive", false);
        model.addAttribute("isToxinActive", true);
        model.addAttribute("isDermalFillerActive", false);
        model.addAttribute("isBiostimulatorsActive", false);

        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", false);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "toxin";
    }

    @GetMapping(value = { "/dermal-filler"})
    public String dermalFillerPage(Model model) {
        model.addAttribute("isHomeActive", false);

        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIvyTherapyActive", false);
        model.addAttribute("isToxinActive", false);
        model.addAttribute("isDermalFillerActive", true);
        model.addAttribute("isBiostimulatorsActive", false);

        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", false);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "dermal-filler";
    }

    @GetMapping(value = { "/biostimulators"})
    public String biostimulatorsPage(Model model) {
        model.addAttribute("isHomeActive", false);

        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIvyTherapyActive", false);
        model.addAttribute("isToxinActive", false);
        model.addAttribute("isDermalFillerActive", false);
        model.addAttribute("isBiostimulatorsActive", true);

        model.addAttribute("isPriceListActive", false);
        model.addAttribute("isBlogActive", false);
        model.addAttribute("isContactUsActive", false);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "biostimulators";
    }
}

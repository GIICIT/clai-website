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

    @GetMapping(value = { "/ivy-therapy"})
    public String ivyTherapyPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIvyTherapyActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "ivy-therapy";
    }

    @GetMapping(value = { "/toxin"})
    public String toxinPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isToxinActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "toxin";
    }

    @GetMapping(value = { "/dermal-filler"})
    public String dermalFillerPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isDermalFillerActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "dermal-filler";
    }

    @GetMapping(value = { "/biostimulators"})
    public String biostimulatorsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isBiostimulatorsActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "biostimulators";
    }

    @GetMapping(value = { "/hyper-pigmentation"})
    public String hyperPigmentationsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isHyperPigmentationActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "hyper-pigmentation";
    }

    @GetMapping(value = { "/acne-and-acne-scaring"})
    public String acneAndAcneScaringPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isAcneAndAcneScaringActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "acne-and-acne-scaring";
    }

    @GetMapping(value = { "/rosacea-and-sensitive-skin"})
    public String rosaceaAndSensitiveSkinPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isRosaceaAndSensitiveSkinActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "rosacea-and-sensitive-skin";
    }

    @GetMapping(value = { "/hair-treatments"})
    public String hairTreatmentsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isHairTreatmentsActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "hair-treatments";
    }

    @GetMapping(value = { "/intimate-wellness"})
    public String intimateWellnessPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIntimateWellnessActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "intimate-wellness";
    }

    @GetMapping(value = { "/anti-ageing"})
    public String antiAgeingPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isAntiAgeingActive", true);
        model.addAttribute("facebook", socialMediaLinksModel.getFacebook());
        model.addAttribute("instagram", socialMediaLinksModel.getInstagram());
        return "anti-ageing";
    }
}

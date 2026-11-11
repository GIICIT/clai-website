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
        return "ivy-therapy";
    }

    @GetMapping(value = { "/toxin"})
    public String toxinPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isToxinActive", true);
        return "toxin";
    }

    @GetMapping(value = { "/dermal-filler"})
    public String dermalFillerPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isDermalFillerActive", true);
        return "dermal-filler";
    }

    @GetMapping(value = { "/biostimulators"})
    public String biostimulatorsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isBiostimulatorsActive", true);
        return "biostimulators";
    }

    @GetMapping(value = { "/hyper-pigmentation"})
    public String hyperPigmentationsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isHyperPigmentationActive", true);
        return "hyper-pigmentation";
    }

    @GetMapping(value = { "/acne-and-acne-scarring"})
    public String acneAndAcneScaringPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isAcneAndAcneScarringActive", true);
        return "acne-and-acne-scarring";
    }

    @GetMapping(value = { "/rosacea-and-sensitive-skin"})
    public String rosaceaAndSensitiveSkinPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isRosaceaAndSensitiveSkinActive", true);
        return "rosacea-and-sensitive-skin";
    }

    @GetMapping(value = { "/hair-treatments"})
    public String hairTreatmentsPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isHairTreatmentsActive", true);
        return "hair-treatments";
    }

    @GetMapping(value = { "/intimate-wellness"})
    public String intimateWellnessPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isIntimateWellnessActive", true);
        return "intimate-wellness";
    }

    @GetMapping(value = { "/anti-ageing"})
    public String antiAgeingPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isAntiAgeingActive", true);
        return "anti-ageing";
    }

    @GetMapping(value = { "/skin-boosters"})
    public String skinBoostersPage(Model model) {
        model.addAttribute("isTreatmentsActive", true);
        model.addAttribute("isSkinBoostersActive", true);
        return "skin-boosters";
    }
}

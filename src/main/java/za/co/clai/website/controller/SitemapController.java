//package za.co.clai.website.controller;
//
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/sitemap")
//public class SitemapController {
//    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> getSitemap() {
//        List<String> urls = Arrays.asList(
//                "https://clai.co.za/",
//                "https://clai.co.za/home",
//                "https://clai.co.za/skin-treatments",
//                "https://clai.co.za/blog-single-why-clai",
//                "https://clai.co.za/contact-us"
//        );
//
//        String xmlContent = generateSitemapXml(urls);
//        return ResponseEntity.ok(xmlContent);
//    }
//
//    private String generateSitemapXml(List<String> urls) {
//        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
//                + urls.stream()
//                .map(url -> "  <url>\n    <loc>" + url + "</loc>\n  </url>")
//                .collect(Collectors.joining("\n"))
//                + "\n</urlset>";
//    }
//}

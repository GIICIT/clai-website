package za.co.clai.website.controller;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class SitemapController {
    @GetMapping("/sitemap")
    public String generateSitemap(HttpServletResponse response) throws IOException {

        String rootPath = System.getProperty("user.dir");
        File baseDir = new File(rootPath);

        WebSitemapGenerator sitemapGenerator = WebSitemapGenerator
                .builder("https://born2roamafrica.co.za", baseDir)
                .build();

        sitemapGenerator.addUrl("https://born2roamafrica.co.za/");
//        sitemapGenerator.addUrl("https://yourwebsite.com/tours/okavango-delta");
        sitemapGenerator.write();

        return "Sitemap generated and saved to: " + new File(baseDir, "sitemap.xml").getAbsolutePath();
    }
}

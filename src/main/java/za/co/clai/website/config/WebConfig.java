package za.co.clai.website.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import java.util.concurrent.TimeUnit;

/**
 * Static resource caching.
 *
 * CSS and JS are served under content-hashed URLs (e.g. /css/style-3f2a...e1.css): Thymeleaf links written
 * as th:href="@{/css/...}" / th:src="@{/js/...}" are rewritten automatically (ResourceUrlEncodingFilter,
 * enabled by spring.web.resources.chain.enabled). Because the URL changes whenever the file changes, they
 * can be cached for a year. Images keep their names, so they get a shorter cache and are then revalidated
 * (Last-Modified / 304).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${video.external.path}")
    private String externalVideoPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/videos/**")
                .addResourceLocations("file:" + externalVideoPath);

        registry
                .addResourceHandler("/css/**", "/js/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().immutable())
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));

        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());

        registry
                .addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic());
    }
}

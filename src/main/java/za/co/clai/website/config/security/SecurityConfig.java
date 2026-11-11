package za.co.clai.website.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * The site has no login: every page and static file is public. Spring Security is kept for
 * CSRF protection on the contact form (POST /email) and its default security headers
 * (HSTS, X-Frame-Options, X-Content-Type-Options).
 *
 * Unknown URLs now fall through to Spring MVC and get a real 404 page (templates/error/404.html)
 * instead of the previous redirect to "/", which search engines treated as soft 404s.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}

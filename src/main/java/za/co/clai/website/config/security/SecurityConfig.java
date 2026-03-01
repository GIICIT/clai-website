package za.co.clai.website.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
                        .requestMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/sass/**", "/docs/**", "/videos/**",
                                "/", "/home", "/ivy-therapy", "/toxin", "/dermal-filler", "/biostimulators","/contact-us", "/email",
                                "/404", "/blog-single-why-clai", "/winter-skin-prep", "/sitemap.xml", "/robots.txt").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/404");
                        })
                );
        return http.build();
    }
}

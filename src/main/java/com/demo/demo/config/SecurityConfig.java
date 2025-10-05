package com.demo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    TODO: Enable the following bean for the security
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests()
//                // Allow public access to authentication endpoints and static resources
//                .requestMatchers("/auth/**", "/static/**").permitAll()
//                // Require authentication for all other requests
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(new JwtAuthenticationFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // Add the filter before Spring's default authentication filter
//
//        return http.build();
//    }

    //    TODO: Delete the bean when api security is needed
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                // Allow public access to all requests
                .requestMatchers("*").permitAll()
                // Require authentication for all other requests (this line will now have no effect since all are permitted above)
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // Add the filter before Spring's default authentication filter

        // Disable CORS within Spring Security (alternative to WebMvcConfigurer)
        http.cors();

        return http.build();
    }

    //    TODO: Delete the bean when api security is needed

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("*") // Allow requests from any origin
                        .allowedMethods("*") // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }
}


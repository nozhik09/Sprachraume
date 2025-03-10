package com.example.Sprachraume.security;

import com.example.Sprachraume.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter filter) {
        this.tokenFilter = filter;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/update").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/invite").permitAll()
                        .requestMatchers(HttpMethod.POST, "room/participant/accept").permitAll()
                        .requestMatchers(HttpMethod.POST, "room/participant/decline").permitAll()
                        .requestMatchers(HttpMethod.POST, "/language/addNativeLanguage").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/nativeLanguage").permitAll()
                        .requestMatchers(HttpMethod.POST, "/language/addLearningLanguage").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/learningLanguages").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/learningnative").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/language/deleteLearning").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/language/deleteNative").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login/google")
//                        .defaultSuccessUrl("/home", true))
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Разрешаем доступ с вашего React приложения
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}



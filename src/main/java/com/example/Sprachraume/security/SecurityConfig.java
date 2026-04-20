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
                        .requestMatchers(HttpMethod.PUT, "/users/block").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/unlock").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/setAdmin").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/delete").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/blockingUsers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/rating").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/ratingBetween").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/findAnyUsers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/avatar/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/file/avatar/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/uploadAvatar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/id").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/allRoom").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/room/participant/invite/accept").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/room/participant/invite/decline").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/participant/invite/sendInvitation").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/participant/invite/getAccept").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/participant/invite/getPending").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/participant/invite/received").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/room/extendTime").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/adminRoom/invite").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/adminRoom/checkPendingInvite").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/adminRoom/invite/participant").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/adminRoom/invite/accept").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/room/adminRoom/accept").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/room/adminRoom/decline").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/allRoom").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/filter").permitAll()
                        .requestMatchers(HttpMethod.POST, "/room/online").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/room/online").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/roomStatus").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/adminRoom/allRoom").permitAll()
                        .requestMatchers(HttpMethod.GET, "/room/active").permitAll()
                        .requestMatchers(HttpMethod.POST, "/language/addNativeLanguage").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/nativeLanguage").permitAll()
                        .requestMatchers(HttpMethod.POST, "/language/addLearningLanguage").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/learningLanguages").permitAll()
                        .requestMatchers(HttpMethod.GET, "/language/learningnative").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/language/deleteLearning").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/language/deleteNative").permitAll()
                        .requestMatchers(HttpMethod.POST, "/category/add").permitAll()
                        .requestMatchers(HttpMethod.GET, "/category/allCategory").permitAll()
                        .requestMatchers(HttpMethod.GET, "/notif").permitAll()
                        .requestMatchers(HttpMethod.GET, "/notif/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/notif/userstatus").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/notif/read").permitAll()
                        .requestMatchers("/", "/api", "/actuator/health", "/health", "/favicon.ico , healthcheck.railway.app").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable);
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login/google")
//                        .defaultSuccessUrl("/home", true))
//                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("http://sprachraume-production.up.railway.app");
        corsConfiguration.addAllowedOrigin("https://sprachraume-production.up.railway.app");
        corsConfiguration.addAllowedOrigin("http://localhost:3000");

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}



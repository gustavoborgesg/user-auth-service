package com.example.auth.infra.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfigurations {

    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/index.css").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui.css").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui-bundle.js").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui-standalone-preset.js").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-initializer.js").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/favicon-32x32.png").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/favicon-16x16.png").permitAll()

                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/swagger-config").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/auth/register/full").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/list").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/userRole/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/userRole/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/userRole/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/userRole/list").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/person/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/person/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/person/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/person/list").hasRole("ADMIN")

                        .anyRequest().denyAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

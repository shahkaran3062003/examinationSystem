package com.roima.examinationSystem.config;


import com.roima.examinationSystem.auth.CustomExceptionHandler;
import com.roima.examinationSystem.auth.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${api.adminPrefix}")
    private String adminPrefix;

    @Value("${api.studentPrefix}")
    private String studentPrefix;

    @Value("${api.prefix}")
    private String prefix;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(prefix+"/auth/login",prefix+"/auth/register")
                        .permitAll()
                        .requestMatchers(adminPrefix+"/**").hasRole("ADMIN")
                        .requestMatchers(studentPrefix+"/**").hasAnyRole("ADMIN","STUDENT")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customExceptionHandler)
                        .accessDeniedHandler(customExceptionHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }

    @Bean
    public RoleHierarchyVoter roleHierarchyVoter() {
        return new RoleHierarchyVoter(new RoleHierarchyImpl());
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Removes the default ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

}

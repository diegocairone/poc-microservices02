package com.cairone.apigatewayservice;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults; 

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> 
                exchanges.pathMatchers("/api/**").permitAll().anyExchange().authenticated())
            .oauth2Login(withDefaults());
        http.csrf().disable();
        return http.build();
    }
}

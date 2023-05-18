package com.hiperium.city.tasks.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        LOGGER.info("securityFilterChain - START");
        return httpSecurity
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(withDefaults()))
                .build();
    }
}

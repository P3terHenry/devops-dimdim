package br.com.fiap.devops.dimdim.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SegurancaConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desativa CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // permite tudo
                )
                .formLogin(login -> login.disable()) // desativa o formulário de login
                .httpBasic(basic -> basic.disable()); // desativa autenticação básica
        return http.build();
    }
}

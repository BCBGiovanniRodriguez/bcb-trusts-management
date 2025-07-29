package com.bcb.trust.front.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bcb.trust.front.service.DatabaseUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final int BCRYPT_DEFAULT_STRENGHT = 12;

    @Autowired
    private DatabaseUserDetailsService databaseUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder(BCRYPT_DEFAULT_STRENGHT);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            ).formLogin(f -> f
                .loginPage("/landing")
                .loginProcessingUrl("/login")
                //.failureUrl("/landing?error=true")
                .defaultSuccessUrl("/", true)
                //.successForwardUrl("/balance")
                //.usernameParameter("email")
                //.passwordParameter("access")
                .permitAll()
            ).httpBasic(Customizer.withDefaults())
            //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(BCRYPT_DEFAULT_STRENGHT));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(databaseUserDetailService);

        return daoAuthenticationProvider;
    }
}

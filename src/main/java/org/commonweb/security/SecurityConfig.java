package org.commonweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JWTUtil jwtUtil; // JWTUtil 빈 주입

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/index","/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults()); // Basic 인증 활성화, 필요에 따라 formLogin() 등으로 변경 가능

        return http.build(); */
        // 나중에 인증이 필요해지면 다시 활성화 시킬것.

        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 기능을 비활성화합니다.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());  // 모든 요청에 대해 인증 없이 접근을 허용합니다.

        return http.build();

    }

    // 인증 간편화 로직 - 언젠가 인증 테스트가 필요해지면 다시 활성화 시킬것.
    /*@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }*/

}

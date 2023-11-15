package jtechlog.jtechlogbootsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/login")
                                .permitAll()
                                .requestMatchers("/")
                                // ROLE_ prefixet auto hozzáfűzi
                                .hasAnyRole("USER", "ADMIN")
                )
                .formLogin(conf -> conf
                        .loginPage("/login")
                        .failureHandler(usernameInUrlAuthenticationFailureHandler())
                )
                .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UsernameInUrlAuthenticationFailureHandler usernameInUrlAuthenticationFailureHandler() {
        return new UsernameInUrlAuthenticationFailureHandler();
    }

}

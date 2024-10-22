package by.project.turamyzba.config;

import by.project.turamyzba.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;


    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter, CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request ->
                request.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/profile/**").permitAll()
                        .requestMatchers("/api-docs").permitAll()
                        .requestMatchers("/announcement/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll().anyRequest().authenticated());
        http.sessionManagement(req -> req.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(customAuthenticationProvider);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
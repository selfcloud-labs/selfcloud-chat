package pl.selfcloud.chat.infrastructure.config;

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

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  private final AuthEntryPointJwt authEntryPointJwt;

  public SecurityConfig(AuthEntryPointJwt authEntryPointJwt) {
    this.authEntryPointJwt = authEntryPointJwt;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(AbstractHttpConfigurer::disable)
          .cors(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/api/v1/auth/**", "/index.html", "/chat/**", "/", "/js.js","/styles.css","/stomp.js").permitAll()
              .anyRequest().authenticated()
          )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .exceptionHandling(ex -> {
            ex.authenticationEntryPoint((request, response, authException) -> response.sendError(401, "Unauthorized"));
            ex.accessDeniedHandler((request, response, authException) -> response.sendError(403, "Forbidden"));
          })
          .build();
  }
}

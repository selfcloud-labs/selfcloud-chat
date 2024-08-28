package pl.selfcloud.chat.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.selfcloud.security.api.util.JwtUtil;

@Configuration
public class JwtConfig {

  @Bean
  public JwtUtil jwtUtil(){
    return new JwtUtil();
  }
  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }

}

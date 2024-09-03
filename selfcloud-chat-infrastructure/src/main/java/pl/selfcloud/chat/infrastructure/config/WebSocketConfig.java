package pl.selfcloud.chat.infrastructure.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import pl.selfcloud.security.api.response.ConnValidationResponse;
import pl.selfcloud.security.api.util.JwtUtil;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${frontend.caller.host:http://localhost:8097}")
  private String frontendCallerHost;
  @Autowired
  private final JwtUtil jwtUtil;

  @Value("${selfcloud-chat.security.host}")
  String host;
  @Autowired
  private final WebClient webClient;

  public WebSocketConfig(JwtUtil jwtUtil, WebClient webClient) {
    this.jwtUtil = jwtUtil;
    this.webClient = webClient;
  }


  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
          String externalBearerToken = accessor.getFirstNativeHeader("Authorization");
          if (externalBearerToken != null && externalBearerToken.startsWith("Bearer ")) {
            String externalToken = externalBearerToken.substring(7).trim();

            //TODO do zmiany poniższy try vo jest bez sensu
            try {
              jwtUtil.isTokenExpired(externalToken);
            } catch (ExpiredJwtException ex) {
              ex.printStackTrace();
            }

            try {
              String url = "http://" + host + ":8090/api/v1/auth/validateToken";
              ConnValidationResponse validationResponse = webClient.get()
                  .uri(url)
                  .header(HttpHeaders.AUTHORIZATION, externalBearerToken)
                  .retrieve()
                  .bodyToMono(ConnValidationResponse.class)
                  .block();  // użycie block() aby wywołać synchronicznie

              if (validationResponse != null) {

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    validationResponse.getUsername(), null, validationResponse.getAuthorities());
                accessor.setUser(authentication);
              } else {
                // Jeśli walidacja się nie powiodła, loguj błąd i nie ustawiaj użytkownika
                log.error("Token validation failed");
              }
            } catch (Exception e) {
              log.error("Error during token validation", e);
            }
          } else {
            log.error("No valid Authorization header found");
          }
        }

        return message;
      }

    });
  }


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
    registry.addEndpoint("/chat").withSockJS();;
    registry.addEndpoint("/chat");

  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic");

  }


}

package pl.selfcloud.chat.infrastructure.filter;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.selfcloud.security.api.response.ConnValidationResponse;
import pl.selfcloud.security.api.util.JwtUtil;
import pl.selfcloud.security.api.util.SecurityConstants;


@Component
@Slf4j
public class WebSocketTokenFilter
    implements ChannelInterceptor{

  @Autowired
  private final JwtUtil jwtUtil;

  @Value("${selfcloud-chat.security.host}")
  String host;
  @Autowired
  private final WebClient webClient;
  public WebSocketTokenFilter(JwtUtil jwtUtil, WebClient webClient) {
    this.jwtUtil= jwtUtil;
    this.webClient = webClient;
  }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
      final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

      if (StompCommand.CONNECT == accessor.getCommand()) {
        String externalBearerToken = accessor.getFirstNativeHeader(SecurityConstants.AUTHORIZATION.name());

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
}

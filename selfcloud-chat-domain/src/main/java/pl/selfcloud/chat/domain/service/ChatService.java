package pl.selfcloud.chat.domain.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.selfcloud.chat.api.model.ChatParticipants;
import pl.selfcloud.chat.domain.model.ChatMessage;
import pl.selfcloud.chat.domain.model.ConversationEntity;
import pl.selfcloud.chat.domain.model.UUIDGenerator;
import pl.selfcloud.chat.domain.repository.ConversationRepository;
import pl.selfcloud.customer.api.dto.CustomerDto;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

  private final ConversationRepository conversationRepository;
  private final Set<UUID> uuids = new HashSet<>();

  private final WebClient webClient;
  private static final String CUSTOMER_SERVICE_URL = "http://localhost:8093/api/v1/customers";

  public ChatService(ConversationRepository conversationRepository, WebClient.Builder webClientBuilder) {
    this.conversationRepository = conversationRepository;
    this.webClient = webClientBuilder.build();
  }

  public ConversationEntity saveMessage(ChatMessage message, String fromUserName){

    ConversationEntity entity = ConversationEntity.builder()
        .content(message.getValue())
        .convId(message.getConvId().toString())
        .toUserName(message.getToUserName())
        .fromUserName(fromUserName)
        .time(LocalDateTime.now())
        .deliveryStatus("seen")
        .build();

    conversationRepository.save(entity);
    return entity;
  }

  public UUID getOrCreateConversation(final ChatParticipants participants)
      throws NoSuchAlgorithmException {

    List<CustomerDto> customerDtos = getAllCustomers().block();
    UUID uuid = UUIDGenerator.generateUUID(participants.getFromUser(), participants.getToUser());

    for (CustomerDto customerDto : customerDtos){
      if (customerDto.getEmail().equals(participants.getToUser())){

        if (uuids.contains(uuid)) return uuid;
        else uuids.add(uuid);

        return uuid;
      }
    }


    throw new RuntimeException("Brak usera mordo");

  }

  private Mono<List<CustomerDto>> getAllCustomers() {

    return webClient.get()
        .uri(CUSTOMER_SERVICE_URL)
        .retrieve()
        .bodyToFlux(CustomerDto.class)
        .collectList();
  }
}

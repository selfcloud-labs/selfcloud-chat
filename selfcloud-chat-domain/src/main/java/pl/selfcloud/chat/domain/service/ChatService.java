package pl.selfcloud.chat.domain.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.selfcloud.chat.api.model.conversation.ConversationComponentsDto;
import pl.selfcloud.chat.api.model.message.ChatMessageToReceiveDto;
import pl.selfcloud.chat.domain.model.ChatMessage;
import pl.selfcloud.chat.domain.model.component.ConversationComponents;
import pl.selfcloud.chat.domain.model.Conversation;
import pl.selfcloud.chat.domain.model.exception.CustomerNotFoundException;
import pl.selfcloud.chat.domain.model.uuid.UUIDGenerator;
import pl.selfcloud.chat.domain.model.mapper.ConversationComponentsMapper;
import pl.selfcloud.chat.domain.repository.ChatMessageRepository;
import pl.selfcloud.chat.domain.repository.ConversationRepository;
import pl.selfcloud.customer.api.dto.CustomerDto;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

  private final ConversationRepository conversationRepository;
  private final ChatMessageRepository chatMessageRepository;

  private final WebClient webClient;
  private static final String CUSTOMER_SERVICE_URL = "http://localhost:8093/api/v1/customers";

  public ChatService(ConversationRepository conversationRepository,
      ChatMessageRepository chatMessageRepository, WebClient.Builder webClientBuilder) {
    this.conversationRepository = conversationRepository;
    this.chatMessageRepository = chatMessageRepository;
    this.webClient = webClientBuilder.build();
  }

  public ChatMessage saveMessage(ChatMessageToReceiveDto message, String fromUserName){

    return chatMessageRepository.save(ChatMessage.builder()
        .convId(message.getConvId().toString())
        .fromUserName(fromUserName)
        .content(message.getContent())
        .time(LocalDateTime.now())
        .deliveryStatus(pl.selfcloud.chat.api.model.message.ChatMessageStatus.SEEN)
        .build()
    );
  }

  public UUID getOrCreateConversation(final ConversationComponentsDto componentsDto, final String fromUser)
      throws NoSuchAlgorithmException {

    ConversationComponents components =
        ConversationComponentsMapper.mapToConversationComponents(componentsDto, fromUser);

    List<CustomerDto> customerDtos = getAllCustomers().block();
    if (customerDtos.stream()
        .anyMatch(customer -> customer.getEmail().equals(components.getToUser()))) {

      UUID uuid = UUIDGenerator.generateUUID(components);
      if (conversationRepository.existsByConvId(uuid.toString())){
        return uuid;
      }else{
        conversationRepository.save(
            Conversation.builder()
                .convId(uuid.toString())
                .fromUserName(components.getFromUser())
                .toUserName(components.getToUser())
                .status(pl.selfcloud.chat.api.model.conversation.ConversationStatus.OPEN)
                .build()
        );
        return uuid;
      }
    } else {
      throw new CustomerNotFoundException(components.getToUser());

    }

  }


  private Mono<List<CustomerDto>> getAllCustomers() {

    return webClient.get()
        .uri(CUSTOMER_SERVICE_URL)
        .retrieve()
        .bodyToFlux(CustomerDto.class)
        .collectList();
  }
}

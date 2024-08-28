package pl.selfcloud.chat.web.controller;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.selfcloud.chat.api.model.ChatParticipants;
import pl.selfcloud.chat.domain.model.ChatMessage;
import pl.selfcloud.chat.domain.model.ConversationEntity;
import pl.selfcloud.chat.domain.model.UUIDDeserializer;
import pl.selfcloud.chat.domain.service.ChatService;


@Controller
@Slf4j
@AllArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/chat")
  public void get(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    Principal userPrincipal = headerAccessor.getUser();
    ConversationEntity entity = chatService.saveMessage(chatMessage, userPrincipal.getName());

    // Dynamiczne wysłanie wiadomości do określonego celu na podstawie convId
    String destination = String.format("/topic/%s/messages", chatMessage.getConvId());
    messagingTemplate.convertAndSend(destination, entity);
  }


  @MessageMapping("/start")
  @SendToUser("/topic/init")
  public UUID initializeConversation(@Payload ChatParticipants participants, SimpMessageHeaderAccessor headerAccessor)
      throws NoSuchAlgorithmException {

    Principal userPrincipal = headerAccessor.getUser();
    participants.setFromUser(userPrincipal.getName());
    UUID uuid = chatService.getOrCreateConversation(participants);

    return uuid;
  }
}
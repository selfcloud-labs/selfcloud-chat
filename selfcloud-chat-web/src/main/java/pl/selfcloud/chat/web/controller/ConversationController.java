package pl.selfcloud.chat.web.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.selfcloud.chat.api.model.conversation.ConversationDto;
import pl.selfcloud.chat.domain.model.ChatMessage;
import pl.selfcloud.chat.domain.service.ConversationService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/conversation")
public class ConversationController {

  private ConversationService conversationService;

  @GetMapping("/{convId}")
  public ResponseEntity<ConversationDto> getConversation(@PathVariable final String convId){
    return new ResponseEntity<>(
        conversationService.getConversation(convId),
        HttpStatus.OK
    );
  }

  @GetMapping("/messages/conv/{id}")
  public ResponseEntity<ChatMessage> get(@PathVariable final String id){
    ChatMessage message = conversationService.chatMessage(id);
    return new ResponseEntity<>(
        message,
        HttpStatus.OK
    );
  }

  @GetMapping("/messages/{id}")
  public ResponseEntity<ChatMessage> getById(@PathVariable final Long id){
    ChatMessage message = conversationService.chatMessageById(id);
    return new ResponseEntity<>(
        message,
        HttpStatus.OK
    );
  }
}

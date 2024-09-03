package pl.selfcloud.chat.domain.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.selfcloud.chat.api.model.conversation.ConversationDto;
import pl.selfcloud.chat.domain.model.ChatMessage;
import pl.selfcloud.chat.domain.model.Conversation;
import pl.selfcloud.chat.domain.model.exception.ConversationNotFoundException;
import pl.selfcloud.chat.domain.model.mapper.ConversationMapper;
import pl.selfcloud.chat.domain.repository.ChatMessageRepository;
import pl.selfcloud.chat.domain.repository.ConversationRepository;

@Service
@AllArgsConstructor
public class ConversationService {

  @Autowired
  ConversationRepository conversationRepository;
  @Autowired
  ChatMessageRepository chatMessageRepository;

  public ConversationDto getConversation(final String convId){

    return ConversationMapper.mapToConversationDto(
        conversationRepository
            .findByConvId(convId)
            .orElseThrow(() -> new ConversationNotFoundException(convId))
    );

  }

  public ChatMessage chatMessage(String convId){
    Optional<ChatMessage> message = chatMessageRepository.findByConvId(convId);
    return message.get();
  }

  public ChatMessage chatMessageById(Long convId){
    Optional<ChatMessage> message = chatMessageRepository.findById(convId);
    return message.get();
  }


}

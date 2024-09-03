package pl.selfcloud.chat.domain.model.mapper;

import pl.selfcloud.chat.api.model.conversation.ConversationComponentsDto;
import pl.selfcloud.chat.domain.model.component.ConversationComponents;

public class ConversationComponentsMapper {

  public static ConversationComponents mapToConversationComponents(
      final ConversationComponentsDto dto, final String fromUser){
    return ConversationComponents.builder()
        .fromUser(fromUser)
        .toUser(dto.getToUser())
        .topic(dto.getTopic())
        .id(dto.getId())
        .build();
  }
}

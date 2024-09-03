package pl.selfcloud.chat.domain.model.mapper;

import java.util.stream.Collectors;
import pl.selfcloud.chat.api.model.conversation.ConversationDto;
import pl.selfcloud.chat.domain.model.Conversation;

public class ConversationMapper {

  public static ConversationDto mapToConversationDto(final Conversation conversation){
    return ConversationDto.builder()
        .id(conversation.getId())
        .convId(conversation.getConvId())
        .fromUserName(conversation.getFromUserName())
        .toUserName(conversation.getToUserName())
        .status(conversation.getStatus())
        .messages(conversation.getMessages().stream()
            .map(ChatMessageMapper::mapToChatMessageDto)
            .collect(Collectors.toList()))
        .build();
  }

}

package pl.selfcloud.chat.domain.model.mapper;

import pl.selfcloud.chat.api.model.message.ChatMessageToSendDto;
import pl.selfcloud.chat.domain.model.ChatMessage;

public class ChatMessageMapper {


  public static ChatMessageToSendDto mapToChatMessageDto(final ChatMessage chatMessage){
    return ChatMessageToSendDto.builder()
        .convId(chatMessage.getConvId())
        .fromUserName(chatMessage.getFromUserName())
        .content(chatMessage.getContent())
        .deliveryStatus(chatMessage.getDeliveryStatus())
        .time(chatMessage.getTime())
        .lastModified(chatMessage.getLastModified())
        .build();
  }
}

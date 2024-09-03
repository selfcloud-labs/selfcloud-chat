package pl.selfcloud.chat.api.model.conversation;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.chat.api.model.message.ChatMessageToReceiveDto;
import pl.selfcloud.chat.api.model.message.ChatMessageToSendDto;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ConversationDto {

  private Long id;
  private String convId;
  private String fromUserName;
  private String toUserName;
  private List<ChatMessageToSendDto> messages;
  private ConversationStatus status;

}

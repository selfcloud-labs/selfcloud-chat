package pl.selfcloud.chat.api.model.message;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ChatMessageToSendDto {

  private Long id;

  private String convId;

  private String fromUserName;

  private String content;

  private LocalDateTime time;

  private LocalDateTime lastModified;

  private ChatMessageStatus deliveryStatus;
}

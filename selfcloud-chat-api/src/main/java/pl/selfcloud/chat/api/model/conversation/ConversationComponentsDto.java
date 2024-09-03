package pl.selfcloud.chat.api.model.conversation;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConversationComponentsDto implements Serializable {

  private String toUser;
  private ConversationTopic topic;
  private Long id;

}

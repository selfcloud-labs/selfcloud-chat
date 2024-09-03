package pl.selfcloud.chat.domain.model.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.chat.api.model.conversation.ConversationTopic;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversationComponents {

  private String fromUser;
  private String toUser;
  private ConversationTopic topic;
  private Long id;
}

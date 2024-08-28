package pl.selfcloud.chat.api.model;

import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.SimpleTriggerContext;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatParticipants {

  private String fromUser;
  private String toUser;

}

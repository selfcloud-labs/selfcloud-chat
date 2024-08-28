package pl.selfcloud.chat.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.OneToMany;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.chat.api.model.ChatMessageStatus;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChatMessage {

    private String value;

    private String toUserName;

    @JsonDeserialize(using = UUIDDeserializer.class)
    private UUID convId;

}

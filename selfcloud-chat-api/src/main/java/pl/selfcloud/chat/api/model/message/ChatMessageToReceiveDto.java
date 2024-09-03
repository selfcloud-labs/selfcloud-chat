package pl.selfcloud.chat.api.model.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.selfcloud.chat.api.model.uuid.UUIDDeserializer;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChatMessageToReceiveDto {

    private String content;

    private String toUserName;

    @JsonDeserialize(using = UUIDDeserializer.class)
    private UUID convId;

}

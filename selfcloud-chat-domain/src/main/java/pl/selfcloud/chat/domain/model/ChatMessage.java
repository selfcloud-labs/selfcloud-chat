package pl.selfcloud.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.selfcloud.chat.api.model.message.ChatMessageStatus;


@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "conv_id")
  private String convId;

  @Column(name = "from_user_name")
  private String fromUserName;

  @Column(name = "content")
  private String content;

  @Column(name = "time")
  @CreatedDate
  private LocalDateTime time;

  @Column(name = "last_modified")
  @LastModifiedDate
  private LocalDateTime lastModified;

  @Column(name = "delivery_status")
  @Enumerated
  private ChatMessageStatus deliveryStatus;

  @ManyToOne
  @JoinColumn(name = "conv_id", insertable = false, updatable = false)
  private Conversation conversation;
}

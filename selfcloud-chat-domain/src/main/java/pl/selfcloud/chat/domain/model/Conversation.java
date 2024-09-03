package pl.selfcloud.chat.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.selfcloud.chat.api.model.conversation.ConversationStatus;

@Entity
@Table(name = "conversations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Conversation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "conv_id")
  private String convId;

  @Column(name = "from_user_name")
  private String fromUserName;

  @Column(name = "to_user_name")
  private String toUserName;

  @OneToMany(mappedBy = "convId")
  @Column(name = "conv_id")
  private List<ChatMessage> messages;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ConversationStatus status;

}
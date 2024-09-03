package pl.selfcloud.chat.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.selfcloud.chat.domain.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  Optional<ChatMessage> findByConvId(String convId);
  Optional<ChatMessage> findById(Long convId);
}

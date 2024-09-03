package pl.selfcloud.chat.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.selfcloud.chat.domain.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

//  @Query(
//      "select c from ConversationEntity c where c.toUserName = :toUserName and c.deliveryStatus in ('NOT_DELIVERED', 'DELIVERED') and c.fromUserName = :fromUserName")
//  List<Conversation> findUnseenMessages(
//      @Param("toUserName") Long toUserName, @Param("fromUserName") Long fromUserName);
//
//  @Query(
//      value =
//          "select * from conversation where to_user_name = :toUserName and delivery_status in ('NOT_DELIVERED', 'DELIVERED')",
//      nativeQuery = true)
//  List<Conversation> findUnseenMessagesCount(@Param("toUserName") Long toUserName);

  boolean existsByConvId(String convId);

  Optional<Conversation> findByConvId(String convId);
}
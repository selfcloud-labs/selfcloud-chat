package pl.selfcloud.chat.domain.model.exception;

public class ConversationNotFoundException extends RuntimeException {

  public ConversationNotFoundException(String uuid) {
    super("Conversation with uuid " + uuid + " not found");
  }
}
# selfcloud-chat
Provides real-time messaging functionality for users using WebSockets:
* creating a conversation between users,
* access to messages sent in the past.

## Controller
The [ChatController](selfcloud-chat-web/src/main/java/pl/selfcloud/chat/web/controller/ChatController.java)
allows to create a conversation and a sending messages.

```java
  @MessageMapping("/chat")
  public void get(@Payload ChatMessageToReceiveDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {

    Principal userPrincipal = headerAccessor.getUser();
    ChatMessage chatMessageEntity = chatService.saveMessage(chatMessage, userPrincipal.getName());

    // Dynamiczne wysłanie wiadomości do określonego celu na podstawie convId
    String destination = String.format("/topic/%s/messages", chatMessage.getConvId());
    messagingTemplate.convertAndSend(destination, chatMessageEntity);
  }


  @MessageMapping("/start")
  @SendToUser("/topic/init")
  public UUID initializeConversation(@Payload ConversationComponentsDto componentsDto, SimpMessageHeaderAccessor headerAccessor)
      throws NoSuchAlgorithmException {

    UUID uuid = chatService.getOrCreateConversation(componentsDto, headerAccessor.getUser().getName());

    return uuid;
  }

```

The personality of conversation participants is set using external 
token attached to the request (Selfcloud security mechanism).
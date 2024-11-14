package odiro.chat;

//
//import java.lang.runtime.ObjectMethods;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//    private final ObjectMapper objectMapper;
//    private final ChatService chatService;
//
//    @Override
//    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage textMessage) throws Exception{
//        String payload = textMessage.getPayload();
//        log.info("{}", payload);
//        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
//
//        ChatRoom chatRoom = chatService.findRoomById(chatMessageDto.getRoomId());
//        chatRoom.handlerActions(webSocketSession, chatMessageDto, chatService);
//    }
//}

package odiro.chat;


import org.springframework.messaging.handler.annotation.DestinationVariable;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserSessionService userSessionService;
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatRoomService chatRoomService, UserSessionService userSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.chatRoomService = chatRoomService;
        this.userSessionService = userSessionService;
    }

//    @MessageMapping("/chat/{roomId}")
//    public void sendMessage(@DestinationVariable String roomId, ChatMessage message, @RequestHeader("userId") String userId) {
//        // 메시지 전송
//        messagingTemplate.convertAndSend("/chat/" + roomId, message);
//
//        // 사용자를 해당 방에 참여시킴
////        userSessionService.getOrCreateSession(userId).joinRoom(roomId);
//    }

//    @MessageMapping("/{roomId}")
//    public void sendMessage(@DestinationVariable String roomId, ChatMessage message ) {
//        // 메시지 전송
//        messagingTemplate.convertAndSend("/send/" + roomId, message);

        // 사용자를 해당 방에 참여시킴
//        userSessionService.getOrCreateSession(userId).joinRoom(roomId);
//    }
    @PostMapping("/api/chat/create/room")
    public ChatRoom createRoom(@RequestBody CreateChatDto createChatDto) {
        return chatRoomService.createChatRoom(createChatDto);
    }
    @PostMapping("/api/chat/send")
    public void sendMessage(@RequestBody MessageDto messageDto) {
        // 메시지를 전송할 채팅방 경로 생성
        String destination = "/send/" + messageDto.getRoomId();

        // 메시지를 브로드캐스트
        Message message = new Message();
        message.setSenderName("User " + messageDto.getSenderId()); // 예시로 senderId를 이름처럼 사용
        message.setContent(messageDto.getContent());
        message.setRoomId(messageDto.getRoomId());

        messagingTemplate.convertAndSend(destination, message);
    }
    @GetMapping("/user/{userId}/rooms")
    public Set<String> getJoinedRooms(@PathVariable String userId) {
        return userSessionService.getJoinedRooms(userId);
    }
}
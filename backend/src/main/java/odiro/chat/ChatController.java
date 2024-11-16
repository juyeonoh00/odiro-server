package odiro.chat;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import odiro.config.auth.PrincipalDetails;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserSessionService userSessionService;
    private final ChatRoomRepository chatRoomRepository;


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

    @Operation(summary = "채팅방 생성", description = "본인을 포함한 인원의 채팅방을 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/api/chat/create/room")
    public ChatRoom createRoom(@RequestBody CreateChatDto createChatDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 룸에 들어간 유저는 구독을 해야함
        return chatRoomService.createChatRoom(createChatDto, principalDetails.getMember().getId());
    }

    @Operation(summary = "메세지", description = "메세지를 보냄")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PostMapping("/api/chat/send")
    public void sendMessage(@RequestBody MessageDto messageDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        chatRoomService.sendMessage(messageDto, principalDetails);

    }
    @Operation(summary = "채팅룸 리스트", description = "유저의 모든 채팅룸의 리스트를 보여줌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/api/chatroomList/{userId}")
    public List<ChatRoom> userMessageList(@PathVariable Long userId) {
        return chatRoomRepository.findAllByUserId(userId);
    }

    @Operation(summary = "채팅방", description = "특정 채팅방의 메시지 리스트를 보여줌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/api/chat/messages/{roomId}")
    public List<Message> getMessages(@PathVariable String roomId) {
        return chatRoomService.getMessagesByRoomId(roomId);
    }
//    @GetMapping("/user/{userId}/rooms")
//    public Set<String> getJoinedRooms(@PathVariable String userId) {
//        return userSessionService.getJoinedRooms(userId);
//    }
}
package odiro.chat;


import static java.awt.DefaultKeyboardFocusManager.*;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odiro.domain.member.Member;
import org.springframework.web.socket.WebSocketSession;


import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String roomId;
    private String name;
    @OneToMany
    private Set<Member> users = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name, Set<Member> users){
        this.roomId = roomId;
        this.name = name;
        this.users = users;
    }
//
//    public void handlerActions(WebSocketSession webSocketSession, ChatMessageDto chatMessageDto, ChatService chatService){
//        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)){
//            webSocketSessions.add(webSocketSession);
//            chatMessageDto.setMessage(chatMessageDto.getSender() +"님이 입장하셨습니다.");
//        }
//
//        sendMessage(chatMessageDto, chatService);
//    }
//
//    private <T> void sendMessage(T message, ChatService chatService){
//        webSocketSessions.parallelStream()
//                .forEach(webSocketSession -> chatService.sendMessage(webSocketSession, message));
//    }
}
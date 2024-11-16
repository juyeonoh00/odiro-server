package odiro.chat;

import java.util.*;

import lombok.RequiredArgsConstructor;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import odiro.repository.MemberRepository;
import odiro.service.MemberService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    // 채팅방을 관리할 Map (채팅방 ID -> ChatRoom 객체)
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    // 새로운 채팅방 생성
    public ChatRoom createChatRoom(CreateChatDto createChatDto, Long userId) {
        String roomId = UUID.randomUUID().toString(); Set<Member> chatroomMembers = new HashSet<>();
        chatroomMembers.add(memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID")));
        for (Long id : createChatDto.getMemberIds()) {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
            chatroomMembers.add(member);
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(createChatDto.getRoomName())
                .users(chatroomMembers)
                .build();
        return chatRoomRepository.save(chatRoom);
    }
    // 특정 채팅방 조회
    public ChatRoom getChatRoom(String roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }

    // 모든 채팅방 조회
    public Collection<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    public List<Message> getMessagesByRoomId(String roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public void sendMessage(MessageDto messageDto, PrincipalDetails principalDetails) {
        // 메시지를 전송할 채팅방 경로 생성
        String destination = "/send/" + messageDto.getRoomId();

        // 메시지를 브로드캐스트
        Message message = new Message();
        message.setSenderName(principalDetails.getUsername());
        message.setContent(messageDto.getContent());
        message.setRoomId(messageDto.getRoomId());
        messageRepository.save(message);
        // 본인을 제외하고 메세지를 받아야함
        // 메세지 알림을 줘야함
        messagingTemplate.convertAndSend(destination, message);
    }
}
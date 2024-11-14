package odiro.chat;

import java.util.*;

import lombok.RequiredArgsConstructor;
import odiro.domain.member.Member;
import odiro.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    // 채팅방을 관리할 Map (채팅방 ID -> ChatRoom 객체)
    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    // 새로운 채팅방 생성
    public ChatRoom createChatRoom(CreateChatDto createChatDto) {
        String roomId = UUID.randomUUID().toString(); Set<Member> chatroomMembers = new HashSet<>();

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
}
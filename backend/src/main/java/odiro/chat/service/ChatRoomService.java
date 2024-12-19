package odiro.chat.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import odiro.chat.domain.ChatRoom;
import odiro.chat.domain.Message;
import odiro.chat.dto.CreateChatDto;
import odiro.chat.dto.MessageDto;
import odiro.chat.repository.ChatRoomRepository;
import odiro.chat.repository.MessageRepository;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.member.Member;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import odiro.repository.MemberRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;


    // 새로운 채팅방 생성
    public ChatRoom createChatRoom(CreateChatDto createChatDto, Long userId) {
        String roomId = UUID.randomUUID().toString();

        Set<Member> chatroomMembers = new HashSet<>();
        chatroomMembers.add(memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED)));
        for (Long id : createChatDto.getMemberIds()) {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED));
            chatroomMembers.add(member);
        }
        String roomName = createChatDto.getRoomName();

        if (roomName == null || roomName.isEmpty()) {
            roomName = chatroomMembers.stream()
                    .map(Member::getUsername)
                    .collect(Collectors.joining(", "));
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .roomName(roomName)
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
        message.setSender(principalDetails.getMember());
        message.setContent(messageDto.getContent());
        message.setRoomId(messageDto.getRoomId());
        messageRepository.save(message);
//
//        // 본인을 제외하고 메세지를 받아야함
//        // 메세지 알림을 줘야함
//        for (String recipientId : participants) {
//            // 발신자는 제외하고 메시지 전송
//            if (!recipientId.equals(senderId)) {
//                messagingTemplate.convertAndSendToUser(
//                        recipientId, // 수신자 ID
//                        "/chat/" + roomId, // 목적지
//                        message // 메시지 내용
//                );
//            }
//        }
        messagingTemplate.convertAndSend(destination, message);
    }

    public List<ChatRoom> getChatRoomsByMember(Member member) {
        return chatRoomRepository.findByUsersContaining(member);
    }
}
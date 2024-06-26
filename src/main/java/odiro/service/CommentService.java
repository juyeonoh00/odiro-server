package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.*;
import odiro.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final TodoService todoService;

    public Plan postComment( Long todoId, Long memberId, String content, LocalDateTime writeTime) {

        // Todo 검색
        Todo todo = todoService.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Comment 저장
        Comment comment = new Comment(todo, member, content, writeTime);
        commentRepository.save(comment);

        //저장된 플랜 반환
        return plan;
    }
}

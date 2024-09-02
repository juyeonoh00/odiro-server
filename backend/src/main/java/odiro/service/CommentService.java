package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.*;
import odiro.domain.member.Member;
import odiro.repository.CommentRepository;
import odiro.service.member.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final DayPlanService dayPlanService;

    public Comment postComment( Long dayPlanId, Long memberId, String content, Long planId) {

        // DayPlan 검색
        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new RuntimeException("DayPlan not found"));

        // 멤버 검색
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if(dayPlan.getPlan().getInitializer().getId().equals(planId)) {
            // Comment 저장
            Comment comment = new Comment(dayPlan, member, content);
            commentRepository.save(comment);

            //comment 반환
            return comment;
        }else{
            throw new RuntimeException("플랜 정보가 일치하지 않습니다");
        }
    }

    public Comment updateComment(Long commentId, String newContent, Long memberId, Long planId) {

        //수정할 comment 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        if(comment.getDayPlan().getPlan().getInitializer().getId().equals(memberId)&&comment.getDayPlan().getPlan().getId().equals(planId)) {
            //comment 수정 후 저장
            comment.setContent(newContent);
            commentRepository.save(comment); // Save updated comment

            //comment 반환
            return comment;
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }


    public void deleteComment(Long commentId, Long memberId, Long planId) {

        //수정할 comment 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        if(comment.getDayPlan().getPlan().getInitializer().getId().equals(memberId)&&comment.getDayPlan().getPlan().getId().equals(planId)) {
            //삭제
            commentRepository.delete(comment);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }
}

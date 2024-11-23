package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.*;
import odiro.domain.member.Member;
import odiro.dto.comment.CommentDetailDto;
import odiro.dto.comment.CommentResponse;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import odiro.repository.CommentRepository;
import odiro.repository.DayPlanRepository;
import odiro.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final DayPlanRepository dayPlanRepository;

    public CommentResponse postComment(Long dayPlanId, Long memberId, String content, Long planId) {

        DayPlan dayPlan = dayPlanRepository.findById(dayPlanId)
                .orElseThrow(() -> new CustomException(ErrorCode.DAYPLAN_NOT_FOUND, dayPlanId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUNDED, memberId));

        if(!dayPlan.getPlan().getPlanMembers().stream().anyMatch(pm->pm.getParticipant().getId().equals(member.getId()))) {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, memberId);

        } else if (!dayPlan.getPlan().getId().equals(planId)) {
            throw new CustomException(ErrorCode.INVALID_PLAN_ID, dayPlanId);

        } else{
            Comment newComment = new Comment(dayPlan, member, content);
            commentRepository.save(newComment);
            return new CommentResponse(newComment);
        }
    }

    public CommentResponse updateComment(Long commentId, String newContent, Member member, Long planId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND, commentId));

        if(!comment.getWriter().equals(member))  {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, member.getId());

        } else if (!comment.getDayPlan().getPlan().getId().equals(planId)) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_ID, commentId);

        }
        else{
            comment.setContent(newContent);
            Comment updateComment = commentRepository.save(comment);
            return new CommentResponse(updateComment);
        }
    }


    public void deleteComment(Long commentId, Member member, Long planId) {

        //수정할 comment 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND, commentId));

        if(!comment.getWriter().equals(member)) {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, member.getId());

        } else if (!comment.getDayPlan().getPlan().getId().equals(planId)) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_ID, commentId);

        } else  {
            commentRepository.delete(comment);
        }
    }

    public Page<CommentDetailDto> getCommentsByDayPlanId(Long dayPlanId, int page, Member member) {

        page = (page > 0) ? page - 1 : 0;

        PageRequest pageRequest = PageRequest.of(page, 10);
        Comment comment = commentRepository.findById(dayPlanId)
                .orElseThrow(() -> new CustomException(ErrorCode.DAYPLAN_NOT_FOUND, dayPlanId));

        Page<Comment> commentPage = commentRepository.findByDayPlanIdOrderByWriteTimeDesc(dayPlanId, pageRequest);

        if (!comment.getWriter().equals(member)) {
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, member.getId());
        } else if (commentPage.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_DAY_PLAN_ID, dayPlanId);

        } else  {
            // Comment 엔티티를 CommentDto로 변환
            List<CommentDetailDto> commentDtos = commentPage.getContent().stream()
                    .map(CommentDetailDto::fromEntity)
                    .collect(Collectors.toList());

            // Page 객체로 변환하여 반환
            return new PageImpl<>(commentDtos, pageRequest, commentPage.getTotalElements());
        }

    }
}

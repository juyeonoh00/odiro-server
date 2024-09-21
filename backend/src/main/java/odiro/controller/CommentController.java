package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.Comment;
import odiro.dto.comment.CommentDetailDto;
import odiro.dto.comment.CommentRequest;
import odiro.dto.comment.CommentResponse;
import odiro.dto.comment.EditCommentRequest;
import odiro.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{planId}/comment/create")
    public ResponseEntity<CommentResponse> writeComment(@RequestBody CommentRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Comment savedComment = commentService.postComment(request.getDayPlanId(),principalDetails.getMember().getId(), request.getContent(), planId);

        CommentResponse response = new CommentResponse(savedComment.getId(), savedComment.getWriteTime());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{planId}/comment/edit")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody EditCommentRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Comment updatedComment = commentService.updateComment(request.getId(), request.getContent(), principalDetails.getMember().getId(), planId);

        CommentResponse response = new CommentResponse(updatedComment.getId(), updatedComment.getWriteTime());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}/comment/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        commentService.deleteComment(commentId, principalDetails.getMember().getId(), planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comment/list")
    public ResponseEntity<Page<CommentDetailDto>> getComments(
            @RequestParam Long dayPlanId,
            @RequestParam(defaultValue = "0") int page) {

        page = (page > 0) ? page - 1 : 0;

        // CommentService를 이용하여 페이지 처리된 CommentDto 리스트를 가져옴
        Page<CommentDetailDto> comments = commentService.getCommentsByDayPlanId(dayPlanId, page);

        // 반환
        return ResponseEntity.ok(comments);
    }
}

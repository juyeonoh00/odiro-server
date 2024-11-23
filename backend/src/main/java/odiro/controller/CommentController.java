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

        CommentResponse newComment = commentService.postComment(request.getDayPlanId(),principalDetails.getMember().getId(), request.getContent(), planId);

        return ResponseEntity.ok(newComment);
    }

    @PutMapping("/{planId}/comment/edit")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody EditCommentRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        CommentResponse updatedComment = commentService.updateComment(request.getId(), request.getContent(), principalDetails.getMember(), planId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{planId}/comment/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        commentService.deleteComment(commentId, principalDetails.getMember(), planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comment/list")
    public ResponseEntity<Page<CommentDetailDto>> getComments(
            @RequestParam Long dayPlanId,
            @RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Page<CommentDetailDto> commmentList = commentService.getCommentsByDayPlanId(dayPlanId, page, principalDetails.getMember());

        return ResponseEntity.ok(commmentList);
    }
}

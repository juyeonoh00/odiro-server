package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Comment;
import odiro.dto.comment.CommentRequest;
import odiro.dto.comment.CommentResponse;
import odiro.dto.comment.EditCommentRequest;
import odiro.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponse> writeComment(@RequestBody CommentRequest request) {

        Comment savedComment = commentService.postComment(request.getDayPlanId(),1L, request.getContent());

        CommentResponse response = new CommentResponse(savedComment.getId(), savedComment.getWriteTime());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/comment/edit")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody EditCommentRequest request) {

        Comment updatedComment = commentService.updateComment(request.getId(), request.getContent());

        CommentResponse response = new CommentResponse(updatedComment.getId(), updatedComment.getWriteTime());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

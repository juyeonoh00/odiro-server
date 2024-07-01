package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Comment;
import odiro.domain.Memo;
import odiro.dto.CommentRequest;
import odiro.dto.CommentResponse;
import odiro.dto.PostMemoResponse;
import odiro.service.CommentService;
import odiro.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/plan/{dayPlanId}/memo/create")
    public ResponseEntity<PostMemoResponse> postMemo(@PathVariable("dayPlanId") Long dayPlanId, @RequestBody CommentRequest request) {

        Memo savedMemo = memoService.postMemo(dayPlanId, request.getContent());

        PostMemoResponse response = new PostMemoResponse(savedMemo.getId());

        return ResponseEntity.ok(response);
    }
}

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
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/plan/memo/{memoId}/edit")
    public ResponseEntity<Void> updateMemo(@PathVariable Long memoId, @RequestBody CommentRequest request) {
        memoService.updateMemo(memoId, request.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/plan/memo/{memoId}/edit")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long memoId) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }
}

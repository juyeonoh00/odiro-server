package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Memo;
import odiro.dto.memo.PostMemoRequest;
import odiro.dto.memo.PostMemoResponse;
import odiro.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/plan/{dayPlanId}/memo/create")
    public ResponseEntity<PostMemoResponse> postMemo(@PathVariable("dayPlanId") Long dayPlanId, @RequestBody PostMemoRequest request) {

        Memo savedMemo = memoService.postMemo(dayPlanId, request.getContent());

        PostMemoResponse response = new PostMemoResponse(savedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/memo/{memoId}")
    public ResponseEntity<PostMemoResponse> updateMemo(@PathVariable("memoId") Long memoId, @RequestBody PostMemoRequest request) {

        Memo updatedMemo = memoService.updateMemo(memoId, request.getContent());

        PostMemoResponse response = new PostMemoResponse(updatedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/memo/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("memoId") Long memoId) {

        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }
}

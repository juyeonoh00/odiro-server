package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Memo;
import odiro.dto.memo.EditMemoRequest;
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

    @PostMapping("/memo/create")
    public ResponseEntity<PostMemoResponse> postMemo(@RequestBody PostMemoRequest request) {

        Memo savedMemo = memoService.postMemo(request.getDayPlanId(), request.getContent());

        PostMemoResponse response = new PostMemoResponse(savedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/memo/edit")
    public ResponseEntity<PostMemoResponse> updateMemo(@RequestBody EditMemoRequest request) {

        Memo updatedMemo = memoService.editMemo(request.getId(), request.getContent());

        PostMemoResponse response = new PostMemoResponse(updatedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/memo/delete/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("memoId") Long memoId) {

        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }
}

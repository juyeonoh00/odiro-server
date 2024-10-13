package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.Memo;
import odiro.dto.memo.EditMemoRequest;
import odiro.dto.memo.PostMemoRequest;
import odiro.dto.memo.PostMemoResponse;
import odiro.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/{planId}/memo/create")
    public ResponseEntity<PostMemoResponse> postMemo(@RequestBody PostMemoRequest request, @PathVariable("planId") Long PathdayPlanId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Memo savedMemo = memoService.postMemo(request.getDayPlanId(), request.getContent(), PathdayPlanId, principalDetails.getMember());

        PostMemoResponse response = new PostMemoResponse(savedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{planId}/memo/edit")
    public ResponseEntity<PostMemoResponse> updateMemo(@RequestBody EditMemoRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Memo updatedMemo = memoService.editMemo(request.getId(), request.getContent(),planId, principalDetails.getMember());

        PostMemoResponse response = new PostMemoResponse(updatedMemo.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}/memo/delete/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("memoId") Long memoId, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        memoService.deleteMemo(memoId,planId, principalDetails.getMember());
        return ResponseEntity.noContent().build();
    }
}

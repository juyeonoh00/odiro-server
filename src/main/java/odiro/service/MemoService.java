package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Comment;
import odiro.domain.DayPlan;
import odiro.domain.Member;
import odiro.domain.Memo;
import odiro.repository.CommentRepository;
import odiro.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final DayPlanService dayPlanService;

    public Memo postMemo(Long dayPlanId, String content) {

        // DayPlan 검색
        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new RuntimeException("DayPlan not found"));

        // Comment 저장
        Memo memo = new Memo(dayPlan, content);
        memoRepository.save(memo);

        //저장된 플랜 반환
        return memo;
    }

    public Memo updateMemo(Long memoId, String content) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));

        memo.setContent(content);
        memoRepository.save(memo); // 수정된 Memo 저장

        return memo;
    }

    public void deleteMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));

        memoRepository.delete(memo); // Memo 삭제
    }

}
package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Memo;
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

        // Memo 생성 후 저장
        Memo memo = new Memo(dayPlan, content);
        memoRepository.save(memo);

        //저장된 플랜 반환
        return memo;
    }

    public Memo editMemo(Long memoId, String content) {

        //Memo 검색
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));

        //Memo 수정 후 저장
        memo.setContent(content);
        memoRepository.save(memo);

        return memo;
    }

    public void deleteMemo(Long memoId) {

        //Memo 검색
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));

        // Memo 삭제
        memoRepository.delete(memo);
    }

}
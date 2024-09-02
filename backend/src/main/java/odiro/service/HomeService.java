package odiro.service;

import lombok.AllArgsConstructor;
import odiro.domain.Plan;
import odiro.dto.PlanFilteredDTO;
import odiro.repository.PlanRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HomeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PlanRepository planRepository;
    public List<Plan> getplanFilteredList(String filterNum) {
        //0일경우 1,2를 모두 찾음
        List<String> patterns = generatePatterns(filterNum);
        List<Object> values = new ArrayList<>();

        for (String pattern : patterns) {
            // Redis에서 패턴에 맞는 키 검색
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null) {
                for (String key : keys) {
                    Object value = redisTemplate.opsForValue().get(key);
                    values.add(value);
                }
            }
        }

        List<Long> randomElements = getRandomElements(values, 6);
        // 해당 리스트를 dto 정보를 가져와서 넣기
        List<Plan> planList = planRepository.findByIdIn(randomElements);
        // Dto로 변환해서 넣기
        return planList;
    }

    //랜덤 리스트 생성
    public List<Long> getRandomElements(List<Object> values, int count) {
        Collections.shuffle(values);

        List<Object> selectedValues = values.subList(0, Math.min(count, values.size()));

        return selectedValues.stream()
                .map(object -> Long.valueOf(object.toString()))
                .collect(Collectors.toList());
    }

    // 0->1,2,로 바꾼 패턴 리스트 생성
    private List<String> generatePatterns(String n) {
        List<String> patterns = new ArrayList<>();
        patterns.add(n);

        // n에 포함된 0을 1과 2로 대체하여 모든 가능한 패턴 생성
        for (int i = 0; i < n.length(); i++) {
            if (n.charAt(i) == '0') {
                List<String> newPatterns = new ArrayList<>();
                for (String pattern : patterns) {
                    newPatterns.add(pattern.substring(0, i) + '1' + pattern.substring(i + 1));
                    newPatterns.add(pattern.substring(0, i) + '2' + pattern.substring(i + 1));
                }
                patterns = newPatterns;
            }
        }
        return patterns;
    }
}

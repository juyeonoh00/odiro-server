package odiro.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.redis.RedisService;
import odiro.domain.DayPlan;
import odiro.domain.Plan;
import odiro.dto.dayPlan.DayPlanDto;
import odiro.dto.location.LocationDto;
import odiro.repository.PlanRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class HomeService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PlanRepository planRepository;
    private final RedisService redisService;
    public List<DayPlanDto> getplanFilteredList(String filterNum) {
        //0일경우 1,2를 모두 찾음
        List<String> patterns = generatePatterns(filterNum);
        List<String> values = new ArrayList<>();

        for (String pattern : patterns) {
            // Redis에서 패턴에 맞는 키 검색
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null) {
                for (String key : keys) {
                    List<String> value = redisService.getList(pattern);
                    values = Stream.concat(values.stream(), value.stream())
                            .toList();
                }
            }
        }

        Object randomElements = getRandomElements(values, 9);
        // 해당 리스트를 dto 정보를 가져와서 넣기
        List<Plan> planList = planRepository.findByIdIn((List<Long>) randomElements);
        // plan이 리스트로 있음
        // planList를 돌면서 dayPlan을 가져와서 첫번째 dayPlan의 정보를 가져와서 넣기
        // 각 planList의 첫번째 dayPlan을 DayPlanDto에 저장하고, 해당 dayPlanDto의 location리스트를 LocationDto에 저장
        List<DayPlanDto> dayPlanDtoList = planList.stream()
                .map(plan -> {
                    List<DayPlan> dayPlans = plan.getDayPlans();
                    if (dayPlans != null && !dayPlans.isEmpty()) {
                        DayPlan firstDayPlan = dayPlans.get(0);
                        return DayPlanDto.builder()
                                .id(plan.getId())
                                .title(plan.getTitle())
                                .firstDay(plan.getFirstDay())
                                .lastDay(plan.getLastDay())
                                .locationList(firstDayPlan.getLocations().stream()
                                        .map(location -> LocationDto.builder()
                                                .lat(location.getLat())
                                                .lng(location.getLng())
                                                .imgUrl(location.getImgUrl())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build();
                    }
                        return null; // DayPlans가 비어있다면 null 반환
                })
                .filter(Objects::nonNull) // null 필터링
                .collect(Collectors.toList());
        // Dto로 변환해서 넣기
        return dayPlanDtoList;
    }

    //랜덤 리스트 생성
    public Object getRandomElements(List<String> values, int count) {
        if (values == null){
            return 0;
        }
        List<String> arrvalues = new ArrayList<>(values);
        Collections.shuffle(arrvalues);

        List<String> selectedValues = arrvalues.subList(0, Math.min(count, arrvalues.size()));

        return selectedValues.stream()
                .map(Long::valueOf)
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
                    newPatterns.add(pattern.substring(0, i) + '0' + pattern.substring(i + 1));
                    newPatterns.add(pattern.substring(0, i) + '1' + pattern.substring(i + 1));
                    newPatterns.add(pattern.substring(0, i) + '2' + pattern.substring(i + 1));
                }
                patterns = newPatterns;
            }
        }
        return patterns;
    }

}

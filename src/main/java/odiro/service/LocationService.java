package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.Comment;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Member;
import odiro.repository.CommentRepository;
import odiro.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final DayPlanService dayPlanService;
    private final LocationRepository locationRepository;

    public Location postLocation(Long dayPlanId, String addressName, Long kakaoMapId, String phone, String placeName, String placeUrl, Long lat, Long lng, String roadAddressName, String CategoryGroupName, String imgUrl) {

        // DayPlan 검색
        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new RuntimeException("DayPlan not found with id: " + dayPlanId));

        // Location 저장
        Location location = new Location(dayPlan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, CategoryGroupName, imgUrl);
        locationRepository.save(location);

        //저장된 플랜 반환
        return location;
    }
}
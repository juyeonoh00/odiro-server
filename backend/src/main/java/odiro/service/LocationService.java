package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final DayPlanService dayPlanService;
    private final PlanService planService;
    private final LocationRepository locationRepository;

    public Location postLocation(Long dayPlanId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String imgUrl, String CategoryGroupName, Long planId, Long userId) {

        // DayPlan 검색
        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new RuntimeException("DayPlan not found with id: " + dayPlanId));
        if(dayPlan.getPlan().getInitializer().getId().equals(userId)&&dayPlan.getPlan().getId().equals(planId)) {
            // Location 저장
            Location location = new Location(dayPlan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, imgUrl, CategoryGroupName);
            locationRepository.save(location);
            return location;
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
        //저장된 플랜 반환
    }

    // Location 수정
    public Location updateLocation(Long locationId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String imgUrl, String CategoryGroupName, Long planId, Long userId) {

        // 기존 Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        if(location.getDayPlan().getPlan().getInitializer().getId().equals(userId)&&location.getDayPlan().getPlan().getId().equals(planId)) {
            // 수정된 정보 업데이트
            location.setAddressName(addressName);
            location.setKakaoMapId(kakaoMapId);
            location.setPhone(phone);
            location.setPlaceName(placeName);
            location.setPlaceUrl(placeUrl);
            location.setLat(lat);
            location.setLng(lng);
            location.setRoadAddressName(roadAddressName);
            location.setImgUrl(imgUrl);
            location.setCategoryGroupName(CategoryGroupName);

            // 수정된 Location 저장 및 반환
            return locationRepository.save(location);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }

    // Location 삭제
    public void deleteLocation(Long locationId, Long planId, Long userId) {
        // Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        // 삭제
        if(location.getDayPlan().getPlan().getInitializer().getId().equals(userId)&&location.getDayPlan().getPlan().getId().equals(planId)) {
            locationRepository.delete(location);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }

    // 장소 찜하기
    public Location postWishLocation(Long planId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String imgUrl, String CategoryGroupName, Long PathplanId, Long userId) {

        // Plan 검색
        Plan plan = planService.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        if(plan.getInitializer().getId().equals(userId)&&plan.getId().equals(planId)) {
            // Location 저장
            Location location = new Location(plan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, imgUrl, CategoryGroupName);
            locationRepository.save(location);

            //저장된 플랜 반환
            return location;
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }

    // 찜한 장소를 정식 등록
    public Location registerWishLocation(Long locationId, Long dayPlanId, Long planId, Long userId) {

        // Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));

        if(location.getDayPlan().getPlan().getInitializer().getId().equals(userId)&&location.getDayPlan().getPlan().getId().equals(planId)) {
            // DayPlan 검색
            DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                    .orElseThrow(() -> new RuntimeException("DayPlan not found with id: " + dayPlanId));
            //dayPlan 등록
            location.setDayPlan(dayPlan);
            location.setPlan(null); //찜한 장소에만 있는 Plan정보는 삭제

            locationRepository.save(location);
            return location;
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }
}
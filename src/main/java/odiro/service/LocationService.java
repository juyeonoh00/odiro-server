package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final DayPlanService dayPlanService;
    private final LocationRepository locationRepository;

    public Location postLocation(Long dayPlanId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Long lat, Long lng, String roadAddressName, String CategoryGroupName, String imgUrl) {

        // DayPlan 검색
        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new RuntimeException("DayPlan not found with id: " + dayPlanId));

        // Location 저장
        Location location = new Location(dayPlan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, CategoryGroupName, imgUrl);
        locationRepository.save(location);

        //저장된 플랜 반환
        return location;
    }

    // Location 수정
    public Location updateLocation(Long locationId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Long lat, Long lng, String roadAddressName, String CategoryGroupName, String imgUrl) {

        // 기존 Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));

        // 수정된 정보 업데이트
        location.setAddressName(addressName);
        location.setKakaoMapId(kakaoMapId);
        location.setPhone(phone);
        location.setPlaceName(placeName);
        location.setPlaceUrl(placeUrl);
        location.setLat(lat);
        location.setLng(lng);
        location.setRoadAddressName(roadAddressName);
        location.setCategoryGroupName(CategoryGroupName);
        location.setImgUrl(imgUrl);

        // 수정된 Location 저장 및 반환
        return locationRepository.save(location);
    }

    // Location 삭제
    public void deleteLocation(Long locationId) {

        // Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));

        //삭제
        locationRepository.delete(location);
    }
}
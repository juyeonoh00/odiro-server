package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.dto.location.WishLocationInDetailPage;
import odiro.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

        if(dayPlan.getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId)) &&
                dayPlan.getPlan().getId().equals(planId)) {
            // Location 저장
            // Location 생성
            Location location = new Location(dayPlan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, imgUrl, CategoryGroupName);

            // location_order에 값 저장
            location.setLocationOrder(dayPlan.getLocations().size()); // 새로 추가할 때 리스트의 현재 크기로 순서 설정
            dayPlan.getLocations().add(location);

            locationRepository.save(location);
            dayPlanService.save(dayPlan);

            //저장된 플랜 반환
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
        if(location.getDayPlan().getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                &&location.getDayPlan().getPlan().getId().equals(planId)) {
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
        if(location.getDayPlan().getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                &&location.getDayPlan().getPlan().getId().equals(planId)) {
            locationRepository.delete(location);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }

    // WishLocation 삭제
    public void deleteWishLocation(Long locationId, Long planId, Long userId) {
        // Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        // 삭제
        if(location.getDayPlan().getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                &&location.getDayPlan().getPlan().getId().equals(planId)) {
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
        if(plan.getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                        &&plan.getId().equals(planId)) {
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

        if(location.getDayPlan().getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                &&location.getDayPlan().getPlan().getId().equals(planId)) {
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
    public List<WishLocationInDetailPage> getWishLocationsByPlanId(Long planId) {
        List<Location> wishLocations = locationRepository.findByPlanIdAndDayPlanIsNull(planId);
        return wishLocations.stream()
                .map(this::convertToWishLocationInDetailPage)
                .collect(Collectors.toList());
    }
    private WishLocationInDetailPage convertToWishLocationInDetailPage(Location location) {
        WishLocationInDetailPage dto = new WishLocationInDetailPage();
        dto.setId(location.getId());
        dto.setAddressName(location.getAddressName());
        dto.setKakaoMapId(location.getKakaoMapId());
        dto.setPhone(location.getPhone());
        dto.setPlaceName(location.getPlaceName());
        dto.setPlaceUrl(location.getPlaceUrl());
        dto.setLat(location.getLat());
        dto.setLng(location.getLng());
        dto.setRoadAddressName(location.getRoadAddressName());
        dto.setImgUrl(location.getImgUrl());
        dto.setCategoryGroupName(location.getCategoryGroupName());
        return dto;
    }
}
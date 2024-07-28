package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.dto.dayPlan.PostDayPlanResponse;
import odiro.dto.location.*;
import odiro.service.DayPlanService;
import odiro.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;
    private final DayPlanService dayPlanService;

    @PostMapping("/location/create")
    public ResponseEntity<PostLocationResponse> postLocation(@RequestBody PostLocationRequest request) {


        Location savedLocation = locationService.postLocation(
                request.getDayPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), request.getImgUrl(), request.getCategoryGroupName()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    // 찜하기
    @PostMapping("/wishLocation/create")
    public ResponseEntity<PostLocationResponse> postWishLocation(@RequestBody PostWishLocationRequest request) {


        Location savedLocation = locationService.postWishLocation(
                request.getPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), request.getImgUrl(), request.getCategoryGroupName()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    //찜한것을 DayPlan에 등록
    @PostMapping("/wishLocation/bring")
    public ResponseEntity<PostLocationResponse> registerWishLoction(@RequestBody RegisterWishLocationRequest request) {


        Location savedLocation = locationService.registerWishLocation(
                request.getLocationId(), request.getDayPlanId()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/location/delete/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("locationId") Long locationId) {

        //삭제
        locationService.deleteLocation(locationId);

        //결과 반환
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/location/reorder")
    public ResponseEntity<PostDayPlanResponse> postLocation(@RequestBody ReorderLocationRequest request) {

        DayPlan reorderedDayplan = dayPlanService.reorderLocations(request.getDayPlanId(), request.getReorderedLocationIds());

        PostDayPlanResponse response = new PostDayPlanResponse(reorderedDayplan.getId());

        return ResponseEntity.ok(response);
    }

    /* //장소 수정 불필요
    @PutMapping("/location/{locationId}")
    public ResponseEntity<PostLocationResponse> updateLocation(@PathVariable("locationId") Long locationId, @RequestBody PostLocationRequest request) {

        //수정된 내용 저장
        Location updatedLocation = locationService.updateLocation(
                locationId,
                request.getAddressName(),
                request.getKakaoMapId(),
                request.getPhone(),
                request.getPlaceName(),
                request.getPlaceUrl(),
                request.getLat(),
                request.getLng(),
                request.getRoadAddressName(),
                request.getCategoryGroupName(),
                request.getImgUrl());

        //결과 반환
        PostLocationResponse response = new PostLocationResponse(updatedLocation.getId());
        return ResponseEntity.ok(response);
    }

     */
}

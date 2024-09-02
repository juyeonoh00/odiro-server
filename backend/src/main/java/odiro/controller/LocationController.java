package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.dto.dayPlan.PostDayPlanResponse;
import odiro.dto.location.*;
import odiro.service.DayPlanService;
import odiro.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;
    private final DayPlanService dayPlanService;

    //장소 새로 등록
    @PostMapping("/{planId}/location/create")
    public ResponseEntity<PostLocationResponse> postLocation(@RequestBody PostLocationRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Location savedLocation = locationService.postLocation(
                request.getDayPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), request.getImgUrl(), request.getCategoryGroupName(), planId, principalDetails.getMember().getId()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    //장소 삭제
    @DeleteMapping("/{planId}/location/delete/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("locationId") Long locationId, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        //삭제
        locationService.deleteLocation(locationId, planId, principalDetails.getMember().getId());

        //결과 반환
        return ResponseEntity.noContent().build();
    }

    //장소 DayPlan에서의 배치 순서 변경
    @PostMapping("/{planId}/location/reorder")
    public ResponseEntity<PostDayPlanResponse> postLocation(@RequestBody ReorderLocationRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        DayPlan reorderedDayplan = dayPlanService.reorderLocations(request.getDayPlanId(), request.getReorderedLocationIds(),planId, principalDetails.getMember().getId());

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

    // 찜하기
    @PostMapping("/{planId}/wishLocation/create")
    public ResponseEntity<PostLocationResponse> postWishLocation(@RequestBody PostWishLocationRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        Location savedLocation = locationService.postWishLocation(
                request.getPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), request.getImgUrl(), request.getCategoryGroupName(),planId, principalDetails.getMember().getId()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    //찜한것을 DayPlan에 등록
    @PostMapping("/{planId}/wishLocation/bring")
    public ResponseEntity<PostLocationResponse> registerWishLoction(@RequestBody RegisterWishLocationRequest request, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {


        Location savedLocation = locationService.registerWishLocation(
                request.getLocationId(), request.getDayPlanId(),planId, principalDetails.getMember().getId()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}/wishLocation/delete/{locationId}")
    public ResponseEntity<Void> deleteWishLocation(@PathVariable("locationId") Long locationId, @PathVariable("planId") Long planId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        //삭제
        locationService.deleteLocation(locationId,planId, principalDetails.getMember().getId());

        //결과 반환
        return ResponseEntity.noContent().build();
    }
}

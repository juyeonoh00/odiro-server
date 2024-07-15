package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.domain.Location;
import odiro.dto.location.PostLocationRequest;
import odiro.dto.location.PostLocationResponse;
import odiro.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/plan/location/create")
    public ResponseEntity<PostLocationResponse> postLocation(@RequestBody PostLocationRequest request) {


        Location savedLocation = locationService.postLocation(
                request.getDayPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), request.getCategoryGroupName(), request.getImgUrl()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

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


    @DeleteMapping("/location/delete/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("locationId") Long locationId) {

        //삭제
        locationService.deleteLocation(locationId);

        //결과 반환
        return ResponseEntity.noContent().build();
    }


}

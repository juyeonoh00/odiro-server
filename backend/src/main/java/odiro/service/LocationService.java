package odiro.service;

import lombok.RequiredArgsConstructor;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;

import odiro.dto.location.*;
import odiro.exception.CustomException;
import odiro.exception.ErrorCode;
import odiro.repository.LocationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

import java.io.IOException;


@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final DayPlanService dayPlanService;
    private final PlanService planService;
    private final LocationRepository locationRepository;

    public PostLocationResponse postLocation(Long dayPlanId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String CategoryGroupName, Long planId, Long userId) {

        // https://place.map.kakao.com/placePrint.daum?confirmid={장소 고유 ID} 형식으로 로직 통일 가능
        String url = "https://place.map.kakao.com/placePrint.daum?confirmid=" + kakaoMapId;
        String imagePath = null;

        //이미지 크롤링
        try {   //이미지가 없을경우 예외처리
            Document doc = Jsoup.connect(url).get();
            Elements images = doc.getElementsByClass("thumb_g");
            if (!images.isEmpty()) {
                imagePath = images.get(0).attr("src");
            } else {
                imagePath = null; // or provide a default image path
            }
        } catch (IOException e) {
            e.printStackTrace();
            imagePath = null; // or provide a default image path
        }

        DayPlan dayPlan = dayPlanService.findById(dayPlanId)
                .orElseThrow(() -> new CustomException(ErrorCode.DAYPLAN_NOT_FOUND, dayPlanId));

        if(dayPlan.getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId)) &&
                dayPlan.getPlan().getId().equals(planId)) {
            // Location 저장
            // Location 생성
            Location location = new Location(dayPlan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, imagePath, CategoryGroupName);

            // location_order에 값 저장
            location.setLocationOrder(dayPlan.getLocations().size()); // 새로 추가할 때 리스트의 현재 크기로 순서 설정
            dayPlan.getLocations().add(location);

            locationRepository.save(location);
            dayPlanService.save(dayPlan);

            //저장된 플랜 반환
            return new PostLocationResponse(location.getId(), imagePath);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
        //저장된 플랜 반환
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


    // 장소 찜하기
    public PostLocationResponse postWishLocation(Long planId, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String CategoryGroupName, Long PathplanId, Long userId) {
        // https://place.map.kakao.com/placePrint.daum?confirmid={장소 고유 ID} 형식으로 로직 통일 가능
        String url = "https://place.map.kakao.com/placePrint.daum?confirmid=" + kakaoMapId;
        String imagePath = null;

        //이미지 크롤링
        try {
            Document doc = Jsoup.connect(url).get();

            // "https:" 가 빠진채로 image src가 저장되어 있으므로 "https:" prefix 추가
            imagePath = doc.getElementsByClass("thumb_g").get(0).attr("src");

        } catch (IOException e) {
            e.printStackTrace();
            imagePath = null; // or provide a default image path
        }
        // Plan 검색
        Plan plan = planService.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND, planId));
        if(plan.getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                        &&plan.getId().equals(planId)) {
            Location location = new Location(plan, addressName, kakaoMapId, phone, placeName, placeUrl, lat, lng, roadAddressName, imagePath, CategoryGroupName);
            locationRepository.save(location);
            return new PostLocationResponse(location.getId(), imagePath);
        }else{
            throw new CustomException(ErrorCode.NOT_AUTHERIZED_USER, userId);
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

    public FestivalResearchResponse festivalResearch(FestivalResearchRequest request) {
        String serviceKey = "szHH6COt5YFTsdBxmYiQHMud7PenOjVtlp3UgLc9a16gRpnoLPcSlKecg9w7Rd%2Bhz0bOAHMnfpQfMDx3KaYpNA%3D%3D";
        String apiUrl = "http://apis.data.go.kr/B551011/KorService1/searchFestival1?";

        try {
            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append("MobileOS=ETC");
            urlBuilder.append("&MobileApp=odiro");
            urlBuilder.append("&_type=json");
            urlBuilder.append("&eventStartDate=").append(request.getYyyymmdd());
            urlBuilder.append("&serviceKey=").append(serviceKey);

            // HTTP 요청 수행
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject body = jsonResponse.getJSONObject("response").getJSONObject("body");
                JSONArray items = body.getJSONObject("items").getJSONArray("item");

                List<FestivalDto> festivalList = new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    FestivalDto festival = new FestivalDto(
                            item.optString("addr1", ""),
                            item.optString("addr2", ""),
                            item.optString("eventstartdate", ""),
                            item.optString("eventenddate", ""),
                            item.optString("firstimage", ""),
                            item.optString("firstimage2", ""),
                            item.optString("tel", ""),
                            item.optString("title", "")
                    );
                    festivalList.add(festival);
                }

                // Response 생성 및 반환
                FestivalResearchResponse responseDto = new FestivalResearchResponse(festivalList);
                return responseDto;

            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ErrorCode.INVALID_URL, e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INVALID_URL, e);
        }
    }
    // WishLocation 삭제
    public void deleteWishLocation(Long locationId, Long planId, Long userId) {
        // Location 검색
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        // 삭제
        if(location.getPlan().getPlanMembers().stream().anyMatch(pm -> pm.getParticipant().getId().equals(userId))
                &&location.getPlan().getId().equals(planId)) {
            locationRepository.delete(location);
        }else{
            throw new RuntimeException("유저 정보 혹은 플랜 정보가 일치하지 않습니다");
        }
    }
}
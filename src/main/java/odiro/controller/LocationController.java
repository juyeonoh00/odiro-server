package odiro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odiro.config.auth.PrincipalDetails;
import odiro.domain.DayPlan;
import odiro.domain.Location;
import odiro.domain.Plan;
import odiro.dto.dayPlan.PostDayPlanResponse;
import odiro.dto.location.*;
import odiro.dto.member.HomeResponse;
import odiro.service.DayPlanService;
import odiro.service.LocationService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;
    private final DayPlanService dayPlanService;

    //장소 새로 등록
    @PostMapping("/location/create")
    public ResponseEntity<PostLocationResponse> postLocation(@RequestBody PostLocationRequest request) {

        // https://place.map.kakao.com/placePrint.daum?confirmid={장소 고유 ID} 형식으로 로직 통일 가능
        String url = "https://place.map.kakao.com/placePrint.daum?confirmid=" + request.getKakaoMapId();
        String imagePath = null;

        //이미지 크롤링
        try {
            Document doc = Jsoup.connect(url).get();

            // "https:" 가 빠진채로 image src가 저장되어 있으므로 "https:" prefix 추가
            imagePath = "https:" + doc.getElementsByClass("thumb_g").get(0).attr("src");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Location savedLocation = locationService.postLocation(
                request.getDayPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), imagePath, request.getCategoryGroupName()
        );

        PostLocationResponse response = new PostLocationResponse(savedLocation.getId());

        return ResponseEntity.ok(response);
    }

    //장소 삭제
    @DeleteMapping("/location/delete/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("locationId") Long locationId) {

        //삭제
        locationService.deleteLocation(locationId);

        //결과 반환
        return ResponseEntity.noContent().build();
    }

    //장소 DayPlan에서의 배치 순서 변경
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

    // 찜하기
    @PostMapping("/wishLocation/create")
    public ResponseEntity<PostLocationResponse> postWishLocation(@RequestBody PostWishLocationRequest request) {

        // https://place.map.kakao.com/placePrint.daum?confirmid={장소 고유 ID} 형식으로 로직 통일 가능
        String url = "https://place.map.kakao.com/placePrint.daum?confirmid=" + request.getKakaoMapId();
        String imagePath = null;

        //이미지 크롤링
        try {
            Document doc = Jsoup.connect(url).get();

            // "https:" 가 빠진채로 image src가 저장되어 있으므로 "https:" prefix 추가
            imagePath = "https:" + doc.getElementsByClass("thumb_g").get(0).attr("src");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Location savedLocation = locationService.postWishLocation(
                request.getPlanId(), request.getAddressName(), request.getKakaoMapId(), request.getPhone(), request.getPlaceName(), request.getPlaceUrl(), request.getLat(), request.getLng(), request.getRoadAddressName(), imagePath, request.getCategoryGroupName()
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

    @DeleteMapping("/wishLocation/delete/{locationId}")
    public ResponseEntity<Void> deleteWishLocation(@PathVariable("locationId") Long locationId) {

        //삭제
        locationService.deleteLocation(locationId);

        //결과 반환
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/location/image/crawl")
//    public CrawlResponse extractImageFromWeb(@RequestBody CrawlRequest request) {
//        CrawlResponse response = new CrawlResponse(null);
//        try {
//            // Jsoup을 사용하여 URL에서 HTML을 가져옵니다.
//            Document doc = Jsoup.connect(request.getUrl())
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//                    .timeout(10 * 1000)
//                    .get();
//
//            // 'bg_present' 클래스를 가진 요소를 선택합니다.
//            Elements elements = doc.select(".bg_present");
//
//            if (elements.isEmpty()) {
//                throw new IOException("No elements found with the specified class.");
//            }
//
//            // 선택된 요소의 background-image URL을 출력합니다.
//            for (Element element : elements) {
//                String style = element.attr("style");
//                String imageUrl = extractBackgroundImageUrl(style);
//                if (imageUrl != null) {
//                    response.setImageUrl(imageUrl);
//                    break;  // 이미지 URL을 찾으면 반복문을 중지합니다.
//                }
//            }
//
//            if (response.getImageUrl() == null) {
//                throw new IOException("No background-image found in the specified element.");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 예외 상황에 대해 적절한 응답을 설정합니다.
//            response.setImageUrl("Error: Unable to extract image URL.");
//        }
//
//        return response;
//    }
//
//    private static String extractBackgroundImageUrl(String style) {
//        String url = null;
//        String prefix = "background-image: url(";
//        int startIndex = style.indexOf(prefix);
//        if (startIndex != -1) {
//            startIndex += prefix.length();
//            int endIndex = style.indexOf(")", startIndex);
//            if (endIndex != -1) {
//                url = style.substring(startIndex, endIndex);
//                // URL에서 불필요한 따옴표를 제거합니다.
//                url = url.replace("\"", "").replace("'", "");
//            }
//        }
//        return url;
//    }




//    @PostMapping("/location/image/crawl")
//    public CrawlResponse extractImageFromWeb(CrawlRequest request) {
//        CrawlResponse response = new CrawlResponse(null);
//        try {
//            // 로그인 필요한 경우 인증 정보를 포함하여 로그인
//            Map<String, String> loginCookies = performLoginAndGetCookies();
//
//            // Jsoup을 사용하여 URL에서 HTML을 가져옵니다.
//            Document doc = Jsoup.connect(request.getUrl())
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
//                    .header("Referer", "https://your.referer.url/")
//                    .header("Authorization", "Bearer your-token")  // JWT 또는 다른 인증 토큰이 필요할 경우
//                    .cookies(loginCookies)  // 로그인 시 받은 쿠키를 추가
//                    .timeout(10 * 1000)
//                    .get();
//
//            Elements elements = doc.select(".bg_present");
//            if (!elements.isEmpty()) {
//                String style = elements.get(0).attr("style");
//                String imageUrl = extractBackgroundImageUrl(style);
//                response.setImageUrl(imageUrl);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.setImageUrl("Error: Unable to extract image URL due to " + e.getMessage());
//        }
//        return response;
//    }
//
//    private Map<String, String> performLoginAndGetCookies() throws IOException {
//        // 예시: 로그인 요청을 수행하고 쿠키를 받아오는 메소드
//        Connection.Response loginResponse = Jsoup.connect("https://example.com/login")
//                .data("username", "your-username")
//                .data("password", "your-password")
//                .method(Connection.Method.POST)
//                .execute();
//
//        return loginResponse.cookies();
//    }
//
//    private String extractBackgroundImageUrl(String style) {
//        String prefix = "background-image: url(";
//        int startIndex = style.indexOf(prefix);
//        if (startIndex != -1) {
//            startIndex += prefix.length();
//            int endIndex = style.indexOf(")", startIndex);
//            if (endIndex != -1) {
//                return style.substring(startIndex, endIndex).replace("\"", "").replace("'", "");
//            }
//        }
//        return null;
//    }

//    @PostMapping("/location/image/crawl")
//    public CrawlResponse CrawlResponse(@RequestBody CrawlRequest request) {
//
//        try {
//            // 요청에서 전달받은 URL을 사용하여 HTML을 가져옵니다.
//            Document doc = Jsoup.connect(request.getUrl()).get();
//
//            // 'link_figure' 클래스를 가진 요소를 선택합니다.
//            Elements elements = doc.select(".link_figure img");
//
//            // 선택된 요소에서 data-org-src 속성 값을 반환합니다.
//            for (Element element : elements) {
//                String imageUrl = element.attr("data-org-src");
//                if (!imageUrl.isEmpty()) {
//                    CrawlResponse response = new CrawlResponse(imageUrl);;
//                    return response;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            CrawlResponse response = new CrawlResponse(null);
//            return response;
//        }
//        CrawlResponse response = new CrawlResponse(null);
//        return response;
//    }

//    @PostMapping("/location/image/crawl")
//    public CrawlResponse extractImageFromWeb(@RequestBody CrawlRequest request) {
//        CrawlResponse response = new CrawlResponse(null);
//
//        try {
//            // 요청에서 전달받은 URL을 사용하여 HTML을 가져옴
//            Document doc = Jsoup.connect(request.getUrl()).get();
//
//            // 'thumb_g' 클래스를 가진 요소를 선택하고, src 속성 값을 추출하여 이미지 경로를 완성
//            String imagePath = "https:" + doc.getElementsByClass("thumb_g").get(0).attr("src");
//
//            // CrawlResponse 객체에 이미지 URL을 설정
//            response.setImageUrl(imagePath);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
}

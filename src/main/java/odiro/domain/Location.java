package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private String addressName;
    private String kakaoMapId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private Float lat;
    private Float lng;
    private String roadAddressName;
    private String categoryGroupName;
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_plan_id")
    private DayPlan dayPlan;    //dayPlan에 추가한 장소

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id")
    private Plan plan;  //찜한 장소

    protected Location () {
    }

    // 장소를 바로 DayPlan에 등록할 때의 생성자
    public Location(DayPlan dayPlan, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String imgUrl, String categoryGroupName)
    {
        this.dayPlan = dayPlan;
        this.addressName = addressName;
        this.kakaoMapId = kakaoMapId;
        this.phone = phone;
        this.placeName = placeName;
        this.placeUrl = placeUrl;
        this.lat = lat;
        this.lng = lng;
        this.roadAddressName = roadAddressName;
        this.imgUrl = imgUrl;
        this.categoryGroupName = categoryGroupName;
    }

    // 장소를 Plan에 찜 목록으로 등록할 때의 생성자
    public Location(Plan plan, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Float lat, Float lng, String roadAddressName, String imgUrl, String categoryGroupName)
    {
        this.plan = plan;
        this.addressName = addressName;
        this.kakaoMapId = kakaoMapId;
        this.phone = phone;
        this.placeName = placeName;
        this.placeUrl = placeUrl;
        this.lat = lat;
        this.lng = lng;
        this.roadAddressName = roadAddressName;
        this.imgUrl = imgUrl;
        this.categoryGroupName = categoryGroupName;
    }
}
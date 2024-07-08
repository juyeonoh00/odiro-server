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
    private Long lat;
    private Long lng;
    private String roadAddressName;
    private String categoryGroupName;
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_plan_id")
    private DayPlan dayPlan;

    protected Location () {
    }

    public Location(DayPlan dayPlan, String addressName, String kakaoMapId, String phone, String placeName, String placeUrl, Long lat, Long lng, String roadAddressName, String categoryGroupName, String imgUrl)
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
        this.categoryGroupName = categoryGroupName;
        this.imgUrl = imgUrl;
    }
}
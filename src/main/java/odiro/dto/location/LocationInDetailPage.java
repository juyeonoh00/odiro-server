package odiro.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationInDetailPage {

    private Long id;
    private String addressName;
    private Long kakaoMapId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private Long lat;
    private Long lng;
    private String roadAddressName;
    private String CategoryGroupName;
    private String imgUrl;

}

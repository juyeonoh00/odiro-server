package odiro.dto.location;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LocationInDetailPage {

    private Long id;
    private String addressName;
    private String kakaoMapId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private Long lat;
    private Long lng;
    private String roadAddressName;
    private String imgUrl;
    private String CategoryGroupName;
}

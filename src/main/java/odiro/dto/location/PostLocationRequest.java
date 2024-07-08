package odiro.dto.location;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostLocationRequest {

    private Long dayPlanId;
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

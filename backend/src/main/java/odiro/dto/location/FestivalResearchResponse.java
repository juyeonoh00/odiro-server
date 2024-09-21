package odiro.dto.location;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FestivalResearchResponse {
    private List<FestivalDto> items;

    // 생성자
    public FestivalResearchResponse(List<FestivalDto> items) {
        this.items = items;
    }

    // Getter와 Setter
    public List<FestivalDto> getItems() {
        return items;
    }

    public void setItems(List<FestivalDto> items) {
        this.items = items;
    }
}
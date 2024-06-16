package odiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class HomeResponse {
    private Long planId;
    private String tite;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;
}

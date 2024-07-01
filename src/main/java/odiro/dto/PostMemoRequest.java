package odiro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMemoRequest {

    private Long memberId;
    private String content;

}

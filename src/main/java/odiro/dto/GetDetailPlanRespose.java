package odiro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.Comment;
import odiro.domain.Location;
import odiro.domain.Memo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetDetailPlanRespose {
    private Long planId;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    private InitializerInDetailPage initializer;
    private List<MemberInDetailPage> participants;
    private List<LocationInDetailPage> locations;
    private List<MemoInDetailPage> memos;
    private List<CommentInDetailPage> comments;
}

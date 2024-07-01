package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class DayPlan {
    @Id @GeneratedValue
    @Column(name = "day_plan_id")
    private Long id;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id") //fk명
    private Plan plan;

    @OneToMany(mappedBy = "dayPlan")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "dayPlan")
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "dayPlan")
    private List<Location> locations = new ArrayList<>();

}

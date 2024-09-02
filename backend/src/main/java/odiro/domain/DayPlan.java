package odiro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Entity
@Getter @Setter
public class DayPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_plan_id")
    private Long id;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id") //fk명
    private Plan plan;
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan")
    private List<Comment> comments = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan")
    private List<Memo> memos = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "location_order")
    private List<Location> locations = new ArrayList<>();
}
package odiro.domain;

import jakarta.persistence.*;
import lombok.*;
import odiro.domain.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id @GeneratedValue
    @Column(name = "plan_id")
    private Long id;
    private Boolean isPublic = false;
    private Long planFilter;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="initializer_id")
    private Member initializer;

    @OneToMany(mappedBy = "plan")
    private List<DayPlan> dayPlans = new ArrayList<>();

    @OneToMany(mappedBy = "plan")
    private List<Location> wishLocations = new ArrayList<>();

    public void initPlan(Member initializer, String title, LocalDateTime firstDay, LocalDateTime lastDay, Boolean isPublic, Long planFilter)
    {
        this.initializer = initializer;
        this.title = title;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.isPublic = isPublic;
        this.planFilter = planFilter;

    }


}

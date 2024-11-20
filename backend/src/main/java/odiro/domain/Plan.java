package odiro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import odiro.domain.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    private Boolean isPublic = false;
    private String planFilter;
    private String title;
    private LocalDateTime firstDay;
    private LocalDateTime lastDay;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="initializer_id")
    private Member initializer;

    @JsonIgnore
    @OneToMany(mappedBy = "plan")
    private List<DayPlan> dayPlans = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "plan")
    private List<Location> wishLocations = new ArrayList<>();

    // 추가: Plan과 관련된 PlanMember 리스트
    @JsonIgnore
    @OneToMany(mappedBy = "plan")
    private List<PlanMember> planMembers = new ArrayList<>(); // Plan과 관련된 PlanMember들

    public void initPlan(Member initializer, String title, LocalDateTime firstDay, LocalDateTime lastDay, Boolean isPublic, String planFilter) {
        this.initializer = initializer;
        this.title = title;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.isPublic = isPublic;
        this.planFilter = planFilter;
    }
}
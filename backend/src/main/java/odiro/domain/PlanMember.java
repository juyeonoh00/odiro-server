package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.member.Member;

@Entity
@Getter
@Setter
public class PlanMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
    @JoinColumn(name="participant_id", foreignKey = @ForeignKey(name = "FK_participant_id"))
    private Member participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id", foreignKey = @ForeignKey(name = "FK_plan_id"))
    private Plan plan;
}

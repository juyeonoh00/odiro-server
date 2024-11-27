package odiro.domain;

import jakarta.persistence.*;
import lombok.*;
import odiro.domain.member.Member;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false, foreignKey = @ForeignKey(name = "FK_plan_invitation_plan"))
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false, foreignKey = @ForeignKey(name = "FK_plan_invitation_sender"))
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false, foreignKey = @ForeignKey(name = "FK_plan_invitation_receiver"))
    private Member receiver;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAccepted = false; // 초대 수락 여부
}
package odiro.repository;

import odiro.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.PlanMember;

import java.util.List;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
    List<PlanMember> findByParticipantId(Long participantId);
    List<PlanMember> findByPlanId(Long planId);
}

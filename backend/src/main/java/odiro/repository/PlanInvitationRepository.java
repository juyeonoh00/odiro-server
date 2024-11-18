package odiro.repository;

import odiro.domain.PlanInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanInvitationRepository extends JpaRepository<PlanInvitation, Long> {
    List<PlanInvitation> findByReceiverIdAndIsAcceptedFalse(Long receiverId);
    void deleteByReceiverIdAndPlanId(Long receiverId, Long planId);
}

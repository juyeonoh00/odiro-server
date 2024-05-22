package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.PlanMember;

@Repository
public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
}

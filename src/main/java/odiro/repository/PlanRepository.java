package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

}

package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {

}

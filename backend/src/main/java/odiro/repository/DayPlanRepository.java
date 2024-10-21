package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.DayPlan;


public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
}

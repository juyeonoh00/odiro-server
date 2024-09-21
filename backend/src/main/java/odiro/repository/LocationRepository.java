package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Location;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByPlanIdAndDayPlanIsNull(Long planId);
}

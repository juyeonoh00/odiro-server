package odiro.repository;

import odiro.dto.location.WishLocationInDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Location;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByPlanIdAndDayPlanIsNull(Long planId);

    List<WishLocationInDetailPage> getWishLocationsByPlanId(Long planId);
}

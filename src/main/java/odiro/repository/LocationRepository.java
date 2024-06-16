package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

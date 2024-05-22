package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}

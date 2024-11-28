package odiro.repository;

import io.lettuce.core.dynamic.annotation.Param;
import odiro.dto.location.WishLocationInDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import odiro.domain.Location;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByPlanIdAndDayPlanIsNull(Long planId);

    @Query("SELECT new odiro.dto.location.WishLocationInDetailPage(" +
            "l.id, l.addressName, l.kakaoMapId, l.phone, l.placeName, l.placeUrl, " +
            "l.lat, l.lng, l.roadAddressName, l.imgUrl, l.categoryGroupName) " +
            "FROM Location l WHERE l.plan.id = :planId")
    List<WishLocationInDetailPage> getWishLocationsByPlanId(@Param("planId") Long planId);

}

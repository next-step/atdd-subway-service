package nextstep.subway.fee.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StationFeeRepository extends JpaRepository<StationFee, Long> {
    @Query(value = "select * from station_fee f where ?1 between f.apply_start_distance and f.apply_end_distance", nativeQuery = true)
    Optional<StationFee> findStationFee(int distance);
}

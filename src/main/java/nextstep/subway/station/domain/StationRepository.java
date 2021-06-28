package nextstep.subway.station.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    @Query("select s from Station s where s.id in :stationsIds")
    List<Station> findBySourceAndTarget(@Param("stationsIds") List<Long> stationsIds);
}

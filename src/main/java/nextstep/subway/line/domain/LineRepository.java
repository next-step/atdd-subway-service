package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Query("select distinct m from Line m join fetch m.lineStations.lineStations s where s.station.id in ?1")
    List<Line> findByStationIdIn(List<Long> stationIds);
}

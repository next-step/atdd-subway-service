package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByLine_IdAndUpStation_Id(Long lineId, Long upStationId);
    Optional<Section> findByLine_IdAndDownStation_Id(Long lineId, Long downStationId);

    @Query(value = "select s.* from Section s where s.line_id = ?1 and ( s.up_station_id = ?2 or s.down_station_id = ?2 )", nativeQuery = true)
    List<Section> findByLine_IdAndUpStation_IdOrDownStation_Id(Long lineId, Long stationId);
}

package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByUpStationAndDownStation(Station upStationId, Station downStationId);
}

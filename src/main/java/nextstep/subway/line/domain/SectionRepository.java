package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByUpStationAndDownStation(Station upStation, Station downStation);
}

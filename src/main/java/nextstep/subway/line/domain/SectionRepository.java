package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.station.domain.Station;

public interface SectionRepository extends JpaRepository<Section, Long> {

	List<Section> findByUpStationOrDownStation(Station upStation, Station downStation);
	List<Section> findByUpStationAndDownStation(Station upStation, Station downStation);
}

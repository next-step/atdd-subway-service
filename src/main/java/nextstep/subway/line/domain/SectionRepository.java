package nextstep.subway.line.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.station.domain.Station;

public interface SectionRepository extends JpaRepository<Section, Long> {
	Optional<Section> findByLineAndUpStationAndDownStation(final Line line, final Station upStation,
		final Station downStation);
}

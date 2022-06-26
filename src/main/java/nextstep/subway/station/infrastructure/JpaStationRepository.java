package nextstep.subway.station.infrastructure;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStationRepository extends StationRepository, JpaRepository<Station, Long> {
}
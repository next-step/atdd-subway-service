package nextstep.subway.station.domain;

import nextstep.subway.common.domain.Name;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    boolean existsByName(Name name);

    default Stations findAllStations() {
        return Stations.from(findAll());
    }
}

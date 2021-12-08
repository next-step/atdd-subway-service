package nextstep.subway.station.infrastructure;

import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Override
    List<Station> findAll();

    boolean existsByName(String name);
}
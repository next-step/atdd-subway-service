package nextstep.subway.station.domain;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    void deleteById(Long id);

    Optional<Station> findById(Long id);

    List<Station> findAll();

    Station save(Station toStation);
}

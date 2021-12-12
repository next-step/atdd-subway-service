package nextstep.subway.station.domain;

import java.util.*;

import org.springframework.data.jpa.repository.*;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    List<Station> findAllByIdIn(List<Long> ids);
}

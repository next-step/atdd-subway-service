package nextstep.subway.station.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    List<Station> findByIdIn(Collection<Long> ids);
}

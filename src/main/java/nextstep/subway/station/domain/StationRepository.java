package nextstep.subway.station.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.common.domain.Name;

public interface StationRepository extends JpaRepository<Station, Long> {
    @Override
    List<Station> findAll();

    boolean existsByName(Name name);
}
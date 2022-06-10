package nextstep.subway.line.domain;

import java.util.Optional;
import nextstep.subway.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(value = "select s from Section s "
        + "where s.line = :line "
        + "and s.upStation = :station")
    Optional<Section> findByEqualsUpStation(Line line, Station station);

    @Query(value = "select s from Section s "
        + "where s.line = :line "
        + "and s.downStation = :station")
    Optional<Section> findByEqualsDownStation(Line line, Station station);
}

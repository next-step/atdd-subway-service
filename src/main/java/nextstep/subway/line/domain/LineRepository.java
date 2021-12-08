package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query(value = "select distinct l from Line l " +
            "left join fetch l.sections s " +
            "left join fetch s.sections sc " +
            "left join fetch sc.upStation " +
            "left join fetch sc.downStation " +
            "where l.id = ?1")
    Optional<Line> findLine(Long id);

    @Query(value = "select distinct l from Line l " +
            "left join fetch l.sections s " +
            "left join fetch s.sections sc " +
            "left join fetch sc.upStation " +
            "left join fetch sc.downStation")
    List<Line> findLines();
}

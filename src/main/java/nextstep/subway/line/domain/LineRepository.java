package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l " +
            "from Line l " +
            "left join fetch l.sections.sectionItems s " +
            "left join fetch s.upStation " +
            "left join fetch s.downStation ")
    List<Line> findAllWithSections();

    @Query("select l " +
            "from Line l " +
            "left join fetch l.sections.sectionItems s " +
            "left join fetch s.upStation " +
            "left join fetch s.downStation ")
    Optional<Line> findByIdWithSections(Long id);
}

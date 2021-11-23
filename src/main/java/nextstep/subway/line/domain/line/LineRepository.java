package nextstep.subway.line.domain.line;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select distinct l from Line l " +
            "left join fetch l.sections.sections sec " +
            "left join fetch sec.upStation " +
            "left join fetch sec.downStation " +
            "where l.id = :id")
    Optional<Line> findLineWithSectionsById(@Param("id") Long id);

}

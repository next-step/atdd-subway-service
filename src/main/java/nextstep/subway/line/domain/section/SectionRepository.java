package nextstep.subway.line.domain.section;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @EntityGraph(attributePaths = {"line", "upStation", "downStation"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Section> findAll();
}

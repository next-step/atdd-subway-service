package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    @Override
    @EntityGraph(attributePaths = {"line", "upStation", "downStation"})
    List<Section> findAll();
}

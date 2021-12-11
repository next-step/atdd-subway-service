package nextstep.subway.path.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.line.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}

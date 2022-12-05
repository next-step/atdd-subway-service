package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSectionRepository extends SectionRepository, JpaRepository<Section, Long> {

}

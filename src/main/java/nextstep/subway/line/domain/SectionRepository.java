package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
	List<Section> findAllByUpStationIdOrDownStationId(Long upStationId, Long downStationId);
}

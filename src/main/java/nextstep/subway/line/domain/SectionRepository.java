package nextstep.subway.line.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    // 그냥 전체 섹션을 받아 오면 됨
    List<Section> findAllByUpStationIdOrDownStationId(Long upStationId, Long downStationId);
}

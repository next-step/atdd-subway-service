package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {
	@Query(value = "SELECT DISTINCT se FROM Section se INNER JOIN Line l ON se.line.id = l.id INNER JOIN Station st ON st.id = se.upStation.id OR st.id = se.downStation.id WHERE st.id = :sourceId OR st.id = :targetId")
	List<Section> findDistinctBySection(@Param("sourceId") Long sourceId, @Param("targetId") Long targetId);
}

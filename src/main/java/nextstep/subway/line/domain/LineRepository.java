package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {

	@Query("select l from Line l where l.id in :lineIds")
	List<Line> findAllByLineIds(@Param("lineIds") List<Long> lineIds);
}

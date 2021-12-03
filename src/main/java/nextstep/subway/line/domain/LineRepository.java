package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {

	@Query("select l from Line l "+
	"where l.id in (select distinct sec.line.id from Section sec "
		+ "where sec.upStation.id in :stationIds "
		+ "or sec.downStation.id in :stationIds)")
	List<Line> findAllExistStations(@Param("stationIds") List<Long> stationIds);
}

package nextstep.subway.line.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nextstep.subway.station.domain.Station;

public interface LineRepository extends JpaRepository<Line, Long> {

	@Query("select l from Line l " +
		"where l in (select distinct sec.line from Section sec "
		+ "where sec.upStation in :stations "
		+ "or sec.downStation in :stations)")
	List<Line> findAllExistStations(@Param("stations") List<Station> stations);
}
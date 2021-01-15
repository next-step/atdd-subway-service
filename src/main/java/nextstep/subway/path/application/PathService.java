package nextstep.subway.path.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import nextstep.subway.common.exception.DuplicateSourceAndTargetException;
import nextstep.subway.common.exception.NoSourceStationException;
import nextstep.subway.common.exception.NoTargetStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@Service
@Transactional
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;
	private PathFinder pathFinder;

	public PathService(LineRepository lineRepository,
		StationRepository stationRepository, PathFinder pathFinder) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.pathFinder = pathFinder;
	}

	public PathResponse findShortestPath(Long sourceId, Long targetId) {
		List<Line> allLines = lineRepository.findAll();
		Station sourceStation = stationRepository.findById(sourceId).orElseThrow(() -> new NoSourceStationException());
		Station targetStation = stationRepository.findById(targetId).orElseThrow(() -> new NoTargetStationException());

		validateFindShortestPathCondition(sourceStation, targetStation);
		return pathFinder.getDijkstraShortestPath(allLines, sourceStation, targetStation);
	}

	private void validateFindShortestPathCondition(Station sourceStation, Station targetStation) {
		if(sourceStation.equals(targetStation)){
			throw new DuplicateSourceAndTargetException();
		}
	}

}

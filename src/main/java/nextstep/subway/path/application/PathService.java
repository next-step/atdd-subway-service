package nextstep.subway.path.application;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import nextstep.subway.common.exception.DuplicateSourceAndTargetException;
import nextstep.subway.common.exception.NoSourceStationException;
import nextstep.subway.common.exception.NoTargetStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
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

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findShortestPath(Long sourceId, Long targetId) {
		validateFindShortestPathCondition(sourceId, targetId);
		List<Line> allLines = lineRepository.findAll();
		Station sourceStation = stationRepository.findById(sourceId).orElseThrow(() -> new NoSourceStationException());
		Station targetStation = stationRepository.findById(targetId).orElseThrow(() -> new NoTargetStationException());

		PathFinder pathFinder = new PathFinder(allLines, sourceStation, targetStation);
		return pathFinder.getDijkstraShortestPath();
	}

	private void validateFindShortestPathCondition(Long sourceId, Long targetId) {
		if(sourceId == targetId) {
			throw new DuplicateSourceAndTargetException();
		}
	}

}

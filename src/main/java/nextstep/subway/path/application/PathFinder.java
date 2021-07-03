package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.FindPathValidator;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathFinder {
	private final LineRepository lineRepository;
	private final StationService stationService;
	private final FindPathValidator findPathValidator;
	private final ShortestPathFinder shortestPathFinder;

	public PathFinder(LineRepository lineRepository, StationService stationService, FindPathValidator findPathValidator, ShortestPathFinder shortestPathFinder) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
		this.findPathValidator = findPathValidator;
		this.shortestPathFinder = shortestPathFinder;
	}

	public PathResponse findPath(Long sourceStationId, Long targetStationId) {
		Station sourceStation = stationService.findById(sourceStationId);
		Station targetStation = stationService.findById(targetStationId);
		List<Line> lines = lineRepository.findAll();
		findPathValidator.validate(sourceStationId, targetStationId);
		return shortestPathFinder.findPath(lines, sourceStation, targetStation);
	}
}

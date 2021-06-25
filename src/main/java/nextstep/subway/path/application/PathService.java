package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final StationService stationService;

	private final LineRepository lineRepository;

	public PathService(StationService stationService, LineRepository lineRepository) {
		this.stationService = stationService;
		this.lineRepository = lineRepository;
	}

	public PathResponse findShortestPath(long source, long target) {
		List<Line> lines = this.lineRepository.findAll();
		Station sourceStation = this.stationService.findById(source);
		Station targetStation = this.stationService.findById(target);
		PathFinder pathFinder = new PathFinder(lines);
		Path path = pathFinder.getShortestPath(sourceStation, targetStation);
		return PathResponse.of(path);
	}
}

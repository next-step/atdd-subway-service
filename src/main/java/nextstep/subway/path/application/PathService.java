package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;
	private PathFinder pathFinder;

	public PathService(LineRepository lineRepository, StationRepository stationRepository,
		PathFinder pathFinder) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.pathFinder = pathFinder;
	}

	public PathResponse findShortestPath(Long source, Long target) {
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = stationRepository.findById(source).get();
		Station targetStation = stationRepository.findById(target).get();

		ShortestPath shortestPath = pathFinder.findShortestPath(lines, sourceStation, targetStation);
		return PathResponse.of(shortestPath);
	}
}

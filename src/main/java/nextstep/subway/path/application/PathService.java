package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.TotalLines;
import nextstep.subway.path.domain.PathFinder;
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
		TotalLines lines = new TotalLines(lineRepository.findAll());
		Station sourceStation = stationRepository.findById(source).get();
		Station targetStation = stationRepository.findById(target).get();

		return PathResponse.of(pathFinder.findShortestPath(lines, sourceStation, targetStation));
	}
}

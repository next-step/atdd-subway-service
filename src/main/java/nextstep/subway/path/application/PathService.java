package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

	private LineRepository lineRepository;
	private StationService stationService;

	public PathService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(PathRequest request) {
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		PathFinder pathFinder = new PathFinder(lineRepository.findAll(), source, target);

		pathFinder.findShortestPath(source, target);
		return PathResponse.of(pathFinder.getStationsInShortestPath(), pathFinder.getShortestDistance());
	}
}

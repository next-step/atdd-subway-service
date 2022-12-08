package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathNavigator;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(PathRequest request) {
		PathNavigator navigator = pathNavigator();
		Path path = path(request, navigator);
		return PathResponse.from(path);
	}

	private Path path(PathRequest request, PathNavigator navigator) {
		return navigator.path(
			station(request.getSource()),
			station(request.getTarget())
		);
	}

	private PathNavigator pathNavigator() {
		return PathNavigator.from(lineService.findAll());
	}

	private Station station(long request) {
		return stationService.findById(request);
	}
}

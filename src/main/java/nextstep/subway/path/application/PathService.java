package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
	public static final String SAME_SOURCE_AND_TARGET = "출발역과 도착역이 같습니다.";
	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(PathRequest pathRequest) {
		validate(pathRequest);

		Station source = stationService.findStationById(pathRequest.getSource());
		Station target = stationService.findStationById(pathRequest.getTarget());

		PathFinder pathFinder = new PathFinder(lineService.findAll());
		Path path = pathFinder.findShortestPath(source, target);

		return PathResponse.of(path);
	}

	private void validate(PathRequest request) {
		Long sourceStationId = request.getSource();
		Long targetStationId = request.getTarget();

		if (sourceStationId.equals(targetStationId)) {
			throw new IllegalArgumentException(SAME_SOURCE_AND_TARGET);
		}
	}
}

package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

	private LineService lineService;
	private StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(Long source, Long target) {
		List<Line> lines = lineService.findAll();
		Station sourceStation = stationService.findStationById(source);
		Station targetStation = stationService.findStationById(target);

		return dijkstraShortestPath(lines, sourceStation, targetStation);
	}

	private PathResponse dijkstraShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
		Path path = new Path(lines, sourceStation, targetStation);

		List<StationResponse> stationResponses = path.getShortestStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		int shortestDistance = path.getShortestDistance();

		return PathResponse.of(stationResponses, shortestDistance);
	}


}

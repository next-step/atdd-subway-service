package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Service
public class PathService {
	public static final String SAME_SOURCE_AND_TARGET = "출발역과 도착역이 같습니다.";
	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public PathResponse findShortestPath(PathRequest pathRequest) {
		validatePathRequest(pathRequest);

		Station source = stationService.findStationById(pathRequest.getSource());
		Station target = stationService.findStationById(pathRequest.getTarget());

		List<Line> lines = lineService.findAll();
		ShortestPath shortestPath = new PathFinder(lines).findShortestPath(source.getId(), target.getId());

		List<Station> stations = stationService.findAllStationsByIds(shortestPath.getVertexes());
		Path path = Path.of(stations, shortestPath.getWeight(), findPathLines(lines, grouping(stations)));

		return PathResponse.of(path);
	}

	private void validatePathRequest(PathRequest request) {
		Long sourceStationId = request.getSource();
		Long targetStationId = request.getTarget();

		if (sourceStationId.equals(targetStationId)) {
			throw new IllegalArgumentException(SAME_SOURCE_AND_TARGET);
		}
	}

	private List<Stations> grouping(List<Station> stations) {
		List<Stations> returnStations = new ArrayList<>();
		for (int i = 0; i < stations.size() - 1; i++) {
			returnStations.add(new Stations(Arrays.asList(stations.get(i), stations.get(i + 1))));
		}
		return returnStations;
	}

	private List<Line> findPathLines(List<Line> lines, List<Stations> stations) {
		return lines.stream()
			.filter(line -> line.anyContainsSection(stations))
			.distinct()
			.collect(Collectors.toList());
	}
}

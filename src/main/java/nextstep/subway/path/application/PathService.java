package nextstep.subway.path.application;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public static final String UNCONNECTED_SOURCE_AND_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";
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

		PathFinder pathFinder = new PathFinder(lineService.findAll());
		GraphPath<Long, DefaultWeightedEdge> graphPath = pathFinder.findShortestPath(source.getId(), target.getId());
		validateGraphPath(graphPath);

		List<Station> pathStations = stationService.findAllStationsByIds(graphPath.getVertexList());
		Path path = Path.of(pathStations, graphPath.getWeight());

		return PathResponse.of(path);
	}

	private void validatePathRequest(PathRequest request) {
		Long sourceStationId = request.getSource();
		Long targetStationId = request.getTarget();

		if (sourceStationId.equals(targetStationId)) {
			throw new IllegalArgumentException(SAME_SOURCE_AND_TARGET);
		}
	}

	private void validateGraphPath(GraphPath<Long, DefaultWeightedEdge> graphPath){
		if(Objects.isNull(graphPath)){
			throw new IllegalArgumentException(UNCONNECTED_SOURCE_AND_TARGET);
		}
	}
}

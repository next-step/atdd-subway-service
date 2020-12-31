package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.CustomWeightedEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.NotConnectedStationException;
import nextstep.subway.path.exception.SameStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinder {

	private List<Line> lines;
	private long startStationId;
	private long destStationId;
	private WeightedMultigraph<Long, CustomWeightedEdge> distanceGraph;
	private GraphPath<Long, CustomWeightedEdge> shortestPath;
	private Map<Long, StationResponse> stationIdToStation;

	public PathFinder(List<Line> lines, long startStationId, long destStationId) {
		validateStation(startStationId, destStationId);
		this.lines = lines;
		this.startStationId = startStationId;
		this.destStationId = destStationId;
		this.distanceGraph = generateGraphByDistance();
		this.shortestPath = getShortestPath();
		this.stationIdToStation = getAllStationById();
	}

	public PathResponse getShortestPathResponse() {
		return PathResponse.of(
			getShortestPathStationsByDistance(),
			getShortestDistance(),
			getShortestPathLinesByDistance()
		);
	}

	private GraphPath<Long, CustomWeightedEdge> getShortestPath() {
		GraphPath<Long, CustomWeightedEdge> path = new DijkstraShortestPath<>(
			this.distanceGraph)
			.getPath(this.startStationId, this.destStationId);
		validateShortestPath(path);
		return path;
	}

	private int getShortestDistance() {
		return (int)Math.round(this.shortestPath.getWeight());
	}

	private List<StationResponse> getShortestPathStationsByDistance() {
		return this.shortestPath.getVertexList()
			.stream()
			.map(id -> stationIdToStation.get(id))
			.collect(Collectors.toList());
	}

	private List<LineResponse> getShortestPathLinesByDistance() {
		List<CustomWeightedEdge> edgeList = shortestPath.getEdgeList();
		List<Line> shortedPathLines = new ArrayList<>();
		for (CustomWeightedEdge edge : edgeList) {
			shortedPathLines.addAll(
				findLinesByUpstationIdAndDownStationId((long)edge.getSource(), (long)edge.getTarget()));
		}

		return shortedPathLines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

	private List<Line> findLinesByUpstationIdAndDownStationId(long upStationId, long downStationId) {
		return lines.stream()
			.filter(line -> line.getSectionsByLine()
				.stream()
				.anyMatch(section -> section.isEqualUpstationAndDownStation(upStationId, downStationId)))
			.collect(Collectors.toList());
	}

	private void validateStation(long startStationId, long destStationId) {
		if (startStationId == destStationId) {
			throw new SameStationException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private void validateShortestPath(GraphPath<Long, CustomWeightedEdge> graphPath) {
		if (graphPath == null) {
			throw new NotConnectedStationException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private WeightedMultigraph<Long, CustomWeightedEdge> generateGraphByDistance() {
		WeightedMultigraph<Long, CustomWeightedEdge> graph = new WeightedMultigraph<>(
			CustomWeightedEdge.class);

		this.lines.stream()
			.flatMap(line -> line.getSectionsByLine().stream())
			.forEach(section -> {
				Station upStation = section.getUpStation();
				Station downStation = section.getDownStation();

				graph.addVertex(upStation.getId());
				graph.addVertex(downStation.getId());
				graph.setEdgeWeight(graph.addEdge(upStation.getId(), downStation.getId()),
					section.getDistance());
			});

		return graph;
	}

	private Map<Long, StationResponse> getAllStationById() {
		return this.lines.stream()
			.flatMap(line -> line.getStations().stream())
			.map(StationResponse::of)
			.collect(Collectors.toMap(StationResponse::getId, stationResponse -> stationResponse, (p1, p2) -> p1));
	}
}

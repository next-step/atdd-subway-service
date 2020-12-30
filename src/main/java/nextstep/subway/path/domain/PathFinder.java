package nextstep.subway.path.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.NotConnectedStationException;
import nextstep.subway.path.exception.SameStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinder {

	private List<Line> lines;
	private long startStationId;
	private long destStationId;
	private WeightedMultigraph<Long, DefaultWeightedEdge> distanceGraph;
	private Map<Long, StationResponse> idToStation;

	public PathFinder(List<Line> lines, long startStationId, long destStationId) {
		validateStation(startStationId, destStationId);
		this.lines = lines;
		this.startStationId = startStationId;
		this.destStationId = destStationId;
		this.distanceGraph = generateGraphByDistance();
		this.idToStation = getAllStationById();
	}

	public PathResponse getShortestPath() {
		return PathResponse.of(
			getShortestPathStationsByDistance(),
			getShortestDistance()
		);
	}

	private List<StationResponse> getShortestPathStationsByDistance() {

		GraphPath<Long, DefaultWeightedEdge> graphPath = new DijkstraShortestPath<>(
			this.distanceGraph)
			.getPath(this.startStationId, this.destStationId);
		validateGraph(graphPath);

		return graphPath.getVertexList()
			.stream()
			.map(id -> idToStation.get(id))
			.collect(Collectors.toList());
	}

	private void validateStation(long startStationId, long destStationId) {
		if (startStationId == destStationId) {
			throw new SameStationException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private void validateGraph(GraphPath<Long, DefaultWeightedEdge> graphPath) {
		if (graphPath == null) {
			throw new NotConnectedStationException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private double getShortestDistance() {

		GraphPath<Long, DefaultWeightedEdge> graphPath = new DijkstraShortestPath<>(
			this.distanceGraph)
			.getPath(this.startStationId, this.destStationId);

		return graphPath.getWeight();
	}

	private WeightedMultigraph<Long, DefaultWeightedEdge> generateGraphByDistance() {
		WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
			DefaultWeightedEdge.class);

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

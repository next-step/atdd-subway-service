package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.common.exception.NotFoundException;
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

	public PathFinder(List<Line> lines, long startStationId, long destStationId) {
		validateStation(startStationId, destStationId);
		this.lines = lines;
		this.startStationId = startStationId;
		this.destStationId = destStationId;
		this.distanceGraph = generateGraphByDistance();
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
		List<Station> stations = graphPath.getVertexList()
			.stream()
			.map(this::findStationById)
			.collect(Collectors.toList());

		return stations.stream()
			.map(StationResponse::of)
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

	private List<Station> getAllStation() {
		return this.lines.stream()
			.flatMap(line -> line.getStations().stream())
			.collect(Collectors.toList());
	}

	private Station findStationById(Long stationId) {
		return getAllStation().stream()
			.filter(station -> Objects.equals(station.getId(), stationId))
			.findFirst()
			.orElseThrow(() -> new NotFoundException("역 정보를 찾을 수 없습니다."));

	}
}

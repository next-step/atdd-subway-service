package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.common.exception.NothingException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
	private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;
	private final List<Line> lines;

	private GraphPath<Station, DefaultWeightedEdge> resultPath;

	public PathFinder(List<Line> lines) {
		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		this.lines = lines;
		generateStationGraph();
		this.path = new DijkstraShortestPath<>(graph);
	}

	public void selectShortPath(Station source, Station target) {
		validateStation(source, target);
		this.resultPath = path.getPath(source, target);
	}

	public List<Station> stations() {
		return resultPath.getVertexList();
	}

	public int distance() {
		return (int)resultPath.getWeight();
	}

	private void generateStationGraph() {
		generateStationVertex();
		generateStationEdgeWeight();
	}

	private void generateStationVertex() {
		getStationsAll().forEach(graph::addVertex);
	}

	private List<Station> getStationsAll() {
		return lines.stream()
			.flatMap(it -> it.stations().stream())
			.collect(Collectors.toList());
	}

	private void generateStationEdgeWeight() {
		lines.forEach(this::addStationEdgeWeight);
	}

	private void addStationEdgeWeight(Line line) {
		line.sections().forEach(
			it -> graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance())
		);
	}

	private void validateStation(Station source, Station target) {
		if (source.equals(target)) {
			throw new RuntimeException("출발역과 도착역이 동일합니다.");
		}

		if (getPath(source, target) == null) {
			throw new NothingException("출발역과 도착역이 연결이 되어 있지 않습니다.");
		}
	}

	private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
		return this.path.getPath(source, target);
	}
}

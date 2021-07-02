package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
	private final DijkstraShortestPath dijkstraShortestPath;
	private final Station source;
	private final Station target;

	public Path(List<Line> lines, Station source, Station target) {
		validateSourceIsEqualsToTarget(source, target);
		validateDoesNotBelongToLines(lines, source, target);

		addVertexes(lines);
		addEdgeWeights(lines);

		this.source = source;
		this.target = target;
		dijkstraShortestPath = new DijkstraShortestPath(graph);
	}

	private void validateConnectedSourceToTarget() {
		if (dijkstraShortestPath.getPath(source, target) == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다.");
		}
	}

	private void validateDoesNotBelongToLines(List<Line> lines, Station source, Station target) {
		if (lines.stream().flatMap(line -> line.getStations().stream()).distinct().noneMatch(station -> station.equals(
			source) && station.equals(target))) {
			throw new IllegalArgumentException("출발역 또는 도착역이 존재하지 않습니다.");
		}
	}

	private void validateSourceIsEqualsToTarget(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
	}

	private void addEdgeWeights(List<Line> lines) {
		lines.stream()
			.map(Line::getSections)
			.forEach(sections -> sections.setEdgeWeight(graph));
	}

	private void addVertexes(List<Line> lines) {
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.forEach(station -> graph.addVertex(station));
	}

	public List<Station> getShortestStations() {
		validateConnectedSourceToTarget();
		return dijkstraShortestPath.getPath(source, target).getVertexList();
	}

	public int getShortestDistance() {
		validateConnectedSourceToTarget();
		return (int) dijkstraShortestPath.getPath(source, target).getWeight();
	}
}

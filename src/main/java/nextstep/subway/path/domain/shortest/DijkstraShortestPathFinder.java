package nextstep.subway.path.domain.shortest;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;

public class DijkstraShortestPathFinder implements ShortestPathFinder {

	private final WeightedGraph<Station, DefaultWeightedEdge> graph;

	private DijkstraShortestPathFinder(List<Line> lines) {
		validate(lines);

		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(section -> addEdge(section.getUpStation(), section.getDownStation(), section.getDistance()));
	}

	private void validate(List<Line> lines) {
		if (null == lines) {
			throw new IllegalArgumentException("노선 목록이 있어야 합니다.");
		}
	}

	public static DijkstraShortestPathFinder of(List<Line> lines) {
		return new DijkstraShortestPathFinder(lines);
	}

	private void addEdge(Station station1, Station station2, int weight) {
		graph.addVertex(station1);
		graph.addVertex(station2);
		graph.setEdgeWeight(graph.addEdge(station1, station2), weight);
	}

	public ShortestPath find(Station source, Station target) throws IllegalArgumentException, PathNotFoundException {
		final GraphPath<Station, DefaultWeightedEdge> path = getPath(source, target);
		if (null == path) {
			throw new PathNotFoundException();
		}
		return ShortestPath.of(path.getVertexList(), path.getWeight());
	}

	private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
		final ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm =
			new DijkstraShortestPath<>(graph);
		return shortestPathAlgorithm.getPath(source, target);
	}
}

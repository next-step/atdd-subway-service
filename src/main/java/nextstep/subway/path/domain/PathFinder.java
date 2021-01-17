package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class PathFinder {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
	private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;
	private final List<Line> lines;
	private GraphPath<Station, DefaultWeightedEdge> resultPath;

	public PathFinder(List<Line> lines) {
		this.lines = lines;
		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		this.path = new DijkstraShortestPath<>(graph);

		addAllVertex();
		addAllEdgeWeight();
	}

	private void addAllVertex() {
		List<Station> stations = lines.stream()
				.flatMap(line -> line.getStations().stream())
				.collect(Collectors.toList());
		stations.forEach(graph::addVertex);
	}


	private void addAllEdgeWeight() {
		lines.forEach(this::addAllEdgeWeight);
	}

	private void addAllEdgeWeight(Line line) {
		line.getSections()
				.forEach(section -> graph.setEdgeWeight(createEdge(section), section.getDistance()));
	}

	private DefaultWeightedEdge createEdge(Section section) {
		return graph.addEdge(section.getUpStation(), section.getDownStation());
	}

	public void findShortestPath(Station source, Station target) {
		this.resultPath = path.getPath(source, target);
	}

	public List<Station> getStationsInShortestPath() {
		return resultPath.getVertexList();
	}

	public int getShortestDistance() {
		return (int) resultPath.getWeight();
	}
}

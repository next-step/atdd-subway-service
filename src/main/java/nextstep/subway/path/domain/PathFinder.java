package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		addVertexes(graph, lines);
		addEdges(graph, lines);
	}

	private void addVertexes(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
		List<Station> stations = lines.stream()
			.map(Line::getStationList)
			.distinct()
			.flatMap(List::stream)
			.collect(Collectors.toList());

		stations.forEach(graph::addVertex);
	}

	private void addEdges(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
		List<Section> sections = lines.stream()
			.map(Line::getSectionList)
			.flatMap(List::stream)
			.collect(Collectors.toList());

		sections.forEach(section -> {
			DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
			graph.setEdgeWeight(edge, section.getDistance());
		});
	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder(lines);
	}

	public Path find(Station source, Station target) {
		ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> graphPath = shortestPathAlgorithm.getPath(source, target);

		List<Station> stations = graphPath.getVertexList();
		int distance = (int)graphPath.getWeight();

		return Path.of(stations, distance);
	}
}

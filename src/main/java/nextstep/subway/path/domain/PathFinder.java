package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	public PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		lines.forEach(line -> {
			line.getStations().forEach(graph::addVertex);
			line.getSections().values().forEach(this::addEdgeWithWeight);
		});
	}

	private void addEdgeWithWeight(Section section) {
		DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
		graph.setEdgeWeight(edge, section.getDistance().value());
	}

	public Path findShortestPath(Station source, Station target) {
		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

		return Path.of(graphPath);
	}
}

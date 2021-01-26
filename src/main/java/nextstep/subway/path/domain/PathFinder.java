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
	private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

	public PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		lines.forEach(line -> {
			line.getStations().stream().map(Station::getId).forEach(graph::addVertex);
			line.getSections().forEach(this::addEdgeWithWeight);
		});
	}

	private void addEdgeWithWeight(Section section) {
		DefaultWeightedEdge edge = graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId());
		graph.setEdgeWeight(edge, section.getDistance());
	}

	public GraphPath<Long, DefaultWeightedEdge> findShortestPath(Long source, Long target) {
		DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

		return dijkstraShortestPath.getPath(source, target);
	}
}

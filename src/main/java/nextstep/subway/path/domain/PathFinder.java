package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	public static final String UNCONNECTED_SOURCE_AND_TARGET = "출발역과 도착역이 연결되어 있지 않습니다.";
	private final WeightedMultigraph<Long, LineWeightedEdge> graph;

	public PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph<>(LineWeightedEdge.class);

		lines.forEach(line -> {
			line.getStations().stream().map(Station::getId).forEach(graph::addVertex);
			line.getSections().forEach(this::addEdgeWithWeight);
		});
	}

	private void addEdgeWithWeight(Section section) {
		LineWeightedEdge edge = new LineWeightedEdge(section.getLine().getId());
		graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId(), edge);
		graph.setEdgeWeight(edge, section.getDistance());
	}

	public ShortestPath findShortestPath(Long source, Long target) {
		DijkstraShortestPath<Long, LineWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Long, LineWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
		validateGraphPath(graphPath);

		return new ShortestPath(graphPath.getEdgeList(), graphPath.getVertexList(), (int)graphPath.getWeight());
	}

	private void validateGraphPath(GraphPath<Long, LineWeightedEdge> graphPath) {
		if (Objects.isNull(graphPath)) {
			throw new IllegalArgumentException(UNCONNECTED_SOURCE_AND_TARGET);
		}
	}
}

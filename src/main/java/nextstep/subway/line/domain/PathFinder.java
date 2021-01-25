package nextstep.subway.line.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.station.domain.Station;

public class PathFinder {

	private DijkstraShortestPath dijkstraShortestPath;

	public PathFinder(final List<Line> lines) {
		this.dijkstraShortestPath = build(lines);
	}

	public SubwayPath findPath(final Station source, final Station target) {
		validateFind(source, target);
		GraphPath graphPath = this.dijkstraShortestPath.getPath(source, target);
		if (graphPath == null) {
			throw new IllegalArgumentException();
		}
		return new SubwayPath(graphPath.getVertexList(), Double.valueOf(graphPath.getWeight()).intValue());
	}

	private void validateFind(final Station source, final Station target) {
		if (source == target) {
			throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
		}
	}

	private DijkstraShortestPath build(final List<Line> lines) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		addVertex(graph, lines);
		addEdge(graph, lines);
		return new DijkstraShortestPath(graph);
	}

	private void addVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Line> lines) {
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.forEach(graph::addVertex);
	}

	private void addEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Line> lines) {
		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
				section.getDistance()));
	}

}

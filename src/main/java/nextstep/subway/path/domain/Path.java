package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathCalculateException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Path {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	public Path(List<Line> lines) {
		this.graph = initGraph(lines);
	}

	private WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(List<Line> lines) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		for (Line line : lines) {
			addLine(graph, line);
		}
		return graph;
	}

	private void addLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
		line.getSections().forEachRemaining(section -> {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
			graph.setEdgeWeight(edge, section.getDistance().getWeight());
		});
	}

	public List<Station> calculate(Station source, Station target) {
		validateCalculate(source, target);
		try {
			return new DijkstraShortestPath<>(graph).getPath(source, target).getVertexList();
		} catch (IllegalArgumentException e) {
			throw new PathCalculateException("경로에 포함되어 있지 않은 역입니다.");
		}
	}

	private void validateCalculate(Station source, Station target) {
		if (Objects.equals(source, target)) {
			throw new PathCalculateException("출발지와 도착지가 같습니다.");
		}
	}
}

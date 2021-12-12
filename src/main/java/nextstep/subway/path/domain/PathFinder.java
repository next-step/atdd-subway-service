package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private PathFinder(List<Line> lines) {
		validateTofindShortest(lines);

		this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(this::addSectionIntoGraph);
	}

	private void validateTofindShortest(List<Line> lines) {
		if (null == lines) {
			throw new IllegalArgumentException("노선 목록이 있어야 합니다.");
		}
	}

	private void addSectionIntoGraph(Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
		graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance()
		);
	}

	public ShortestPath findShortest(Station source, Station target) {
		validateToFindShortest(source, target);

		final GraphPath<Station, DefaultWeightedEdge> path = getPath(source, target);
		if (null == path) {
			throw new PathNotFoundException();
		}
		return ShortestPath.of(path.getVertexList(), path.getWeight());
	}

	private void validateToFindShortest(Station source, Station target) {
		if (Objects.equals(source, target)) {
			throw new IllegalArgumentException("경로의 출발역과 도착역은 서로 달라야 합니다.");
		}
	}

	private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
		final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
			new DijkstraShortestPath<>(graph);
		try {
			return dijkstraShortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("노선에 포함되지 않은 역입니다.");
		}
	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder(lines);
	}
}

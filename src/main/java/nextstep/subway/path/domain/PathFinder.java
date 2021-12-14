package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

	private PathFinder() {
		this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
	}

	public static PathFinder of(List<Section> sections) {
		PathFinder pathFinder = new PathFinder();
		sections.forEach(pathFinder::addSection);
		return pathFinder;
	}

	public static PathFinder of(Sections sections) {
		PathFinder pathFinder = new PathFinder();
		sections.toList().forEach(pathFinder::addSection);
		return pathFinder;
	}

	public static PathFinder ofLines(List<Line> lines) {
		List<Section> sections = lines.stream()
			.map(Line::getSections)
			.map(Sections::toList)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		return PathFinder.of(sections);
	}

	public PathResponse findPath(Station source, Station target) {
		validateSourceAndTarget(source, target);
		DijkstraShortestPath path = new DijkstraShortestPath(this.graph);
		GraphPath graphPath = path.getPath(source, target);
		validatePath(graphPath);
		int distance = (int)graphPath.getWeight();
		return PathResponse.of(graphPath.getVertexList(), distance);
	}

	private void addSection(Section section) {
		this.graph.addVertex(section.getUpStation());
		this.graph.addVertex(section.getDownStation());
		this.graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance().toInt());
	}

	private void validateSourceAndTarget(Station source, Station target) {
		if (source.equals(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 같으면 안됩니다");
		}
		if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "존재하지 않는 출발역과 도착역을 조회할 경우 안된다");
		}
	}

	private void validatePath(GraphPath graphPath) {
		if (Objects.isNull(graphPath)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 연결되어 있어야 한다");
		}
	}

}

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

	private final Sections sections;
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph
		= new WeightedMultigraph(DefaultWeightedEdge.class);

	private PathFinder(Sections sections) {
		sections.getSections().forEach(this::addSection);
		this.sections = sections;
	}

	public static PathFinder of(Sections sections) {
		return new PathFinder(sections);
	}

	public static PathFinder ofLines(List<Line> lines) {
		List<Section> sections = lines.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		return new PathFinder(Sections.of(sections));
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
		if (!sections.containStation(source) || !sections.containStation(target)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "존재하지 않는 출발역과 도착역을 조회할 경우 안된다");
		}
	}

	private void validatePath(GraphPath graphPath) {
		if (Objects.isNull(graphPath)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "출발역과 도착역이 연결되어 있어야 한다");
		}
	}

}

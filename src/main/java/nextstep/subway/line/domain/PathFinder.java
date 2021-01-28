package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.exception.CanNotFindPathException;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final GraphPath<Station, SectionEdge> graphPath;
	private final Fare pathLineFare;

	public PathFinder(final List<Line> lines, final Station source, final Station target) {
		validate(source, target);
		this.graphPath = getGraphPath(lines, source, target);
		this.pathLineFare = calculatePathLineAdditionalPare();
	}

	private void validate(final Station source, final Station target) {
		if (source == target) {
			throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
		}
	}

	private GraphPath<Station, SectionEdge> getGraphPath(final List<Line> lines,
		final Station source, final Station target) {
		DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = build(lines);
		GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(source, target);
		if (graphPath == null) {
			throw new CanNotFindPathException(source, target);
		}
		return graphPath;
	}

	private DijkstraShortestPath<Station, SectionEdge> build(final List<Line> lines) {
		WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
		lines.forEach(line -> makePath(graph, line));
		return new DijkstraShortestPath<>(graph);
	}

	private void makePath(final WeightedMultigraph<Station, SectionEdge> graph, final Line line) {
		line.getSections()
			.forEach(section -> {
				addVertex(graph, section);
				addEdge(graph, section);
			});
	}

	private void addVertex(final WeightedMultigraph<Station, SectionEdge> graph, final Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
	}

	private void addEdge(final WeightedMultigraph<Station, SectionEdge> graph, final Section section) {
		SectionEdge sectionEdge = new SectionEdge(section);
		graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
		graph.setEdgeWeight(sectionEdge, section.getDistance());
	}

	public SubwayPath findPath() {
		return new SubwayPath(this.graphPath.getVertexList(),
			Double.valueOf(this.graphPath.getWeight()).intValue(), this.pathLineFare.getFare());
	}

	private Fare calculatePathLineAdditionalPare() {
		return new Fare(findLines().stream()
			.distinct()
			.flatMapToInt(line -> IntStream.of(line.getAdditionalFee().getFare()))
			.max()
			.orElse(0));
	}

	public Fare getPathLineFare() {
		return this.pathLineFare;
	}

	private List<Line> findLines() {
		return graphPath.getEdgeList().stream()
			.map(SectionEdge::getLine)
			.collect(Collectors.toList());
	}
}

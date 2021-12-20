package nextstep.subway.path.domain.shortest;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;

public class DijkstraShortestPathFinder implements ShortestPathFinder {

	private final WeightedGraph<Station, SectionEdge> graph;

	private DijkstraShortestPathFinder(List<Line> lines) {
		validate(lines);

		this.graph = new WeightedMultigraph<>(SectionEdge.class);

		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(this::addEdge);
	}

	private void validate(List<Line> lines) {
		if (null == lines) {
			throw new IllegalArgumentException("노선 목록이 있어야 합니다.");
		}
	}

	public static DijkstraShortestPathFinder of(List<Line> lines) {
		return new DijkstraShortestPathFinder(lines);
	}

	private void addEdge(Section section) {
		final Station v1 = section.getUpStation();
		final Station v2 = section.getDownStation();
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addEdge(v1, v2, SectionEdge.of(section));
	}

	public ShortestPath find(Station source, Station target) throws IllegalArgumentException, PathNotFoundException {
		final GraphPath<Station, SectionEdge> path = getPath(source, target);
		if (null == path) {
			throw new PathNotFoundException();
		}
		final List<Line> lines = path.getEdgeList().stream()
			.map(SectionEdge::getLine)
			.collect(Collectors.toList());
		return ShortestPath.of(lines, path.getVertexList(), path.getWeight());
	}

	private GraphPath<Station, SectionEdge> getPath(Station source, Station target) {
		final ShortestPathAlgorithm<Station, SectionEdge> shortestPathAlgorithm =
			new DijkstraShortestPath<>(graph);
		return shortestPathAlgorithm.getPath(source, target);
	}
}

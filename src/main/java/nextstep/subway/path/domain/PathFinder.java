package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final ShortestPathAlgorithm<Station, SectionEdge> pathFindAlgorithm;
	private final WeightedMultigraph<Station, SectionEdge> stationGraph;

	private PathFinder(WeightedMultigraph<Station, SectionEdge> stationGraph) {
		this.stationGraph = stationGraph;
		this.pathFindAlgorithm = new DijkstraShortestPath<>(stationGraph);
	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder(createGraph(lines));
	}

	public Path findPath(Station source, Station target) {
		validateNotEqual(source, target);
		validateExists(source, target);
		return findStationPath(source, target);
	}

	private static WeightedMultigraph<Station, SectionEdge> createGraph(List<Line> lines) {
		WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
		for (Line line : lines) {
			addVertexesAndEdges(graph, line);
		}
		return graph;
	}

	private static void addVertexesAndEdges(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
		for (Section section : line.getSections()) {
			SectionEdge edge = new SectionEdge(section);
			graph.addVertex(edge.getUpStation());
			graph.addVertex(edge.getDownStation());
			graph.addEdge(edge.getUpStation(), edge.getDownStation(), edge);
			graph.setEdgeWeight(edge, edge.getDistance());
		}
	}

	private Path findStationPath(Station source, Station target) {
		return Optional
			.ofNullable(pathFindAlgorithm.getPath(source, target))
			.map(path -> new Path(path.getVertexList(), path.getEdgeList()))
			.orElseThrow(() -> new PathException("출발역과 도착역이 연결되어 있지 않습니다."));
	}

	private void validateExists(Station source, Station target) {
		if (!stationGraph.containsVertex(source) || !stationGraph.containsVertex(target)) {
			throw new PathException("출발역 또는 도착역이 존재하지 않습니다.");
		}
	}

	private void validateNotEqual(Station source, Station target) {
		if (source.equals(target)) {
			throw new PathException("도착역과 출발역이 같을 수 없습니다.");
		}
	}
}

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

public class StationPathFinder {

	private final ShortestPathAlgorithm<Station, SectionEdge> pathFindAlgorithm;
	private final WeightedMultigraph<Station, SectionEdge> stationGraph;

	private StationPathFinder(WeightedMultigraph<Station, SectionEdge> stationGraph) {
		this.stationGraph = stationGraph;
		this.pathFindAlgorithm = new DijkstraShortestPath<>(stationGraph);
	}

	public static StationPathFinder of(List<Line> lines) {
		return new StationPathFinder(createGraph(lines));
	}

	public StationPath findPath(Station source, Station target) {
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
			graph.addVertex(edge.getSource());
			graph.addVertex(edge.getTarget());
			graph.addEdge(edge.getSource(), edge.getTarget(), edge);
			graph.setEdgeWeight(edge, edge.getWeight());
		}
	}

	private StationPath findStationPath(Station source, Station target) {
		GraphPath<Station, SectionEdge> path = Optional
			.ofNullable(pathFindAlgorithm.getPath(source, target))
			.orElseThrow(() -> new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않습니다."));
		return new ShortestStationPath(path.getVertexList(), path.getEdgeList());
	}

	private void validateExists(Station source, Station target) {
		if (!stationGraph.containsVertex(source) || !stationGraph.containsVertex(target)) {
			throw new IllegalArgumentException("출발역 또는 도착역이 존재하지 않습니다.");
		}
	}

	private void validateNotEqual(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException("도착역과 출발역이 같을 수 없습니다.");
		}
	}
}

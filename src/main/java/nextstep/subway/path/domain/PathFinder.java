package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class PathFinder {
	private final WeightedMultigraph<Station, SectionEdge> graph;
	private List<Station> stations;

	private PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph<>(SectionEdge.class);

		addVertexes(graph, lines);
		addEdges(graph, lines);
	}

	private void addVertexes(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
		stations = lines.stream()
			.map(Line::getStations)
			.map(Stations::getValues)
			.distinct()
			.flatMap(List::stream)
			.collect(Collectors.toList());

		stations.forEach(graph::addVertex);
	}

	private void addEdges(WeightedMultigraph<Station, SectionEdge> graph, List<Line> lines) {
		List<SectionEdge> sectionEdges = lines.stream()
			.map(Line::getSections)
			.map(Sections::getValues)
			.flatMap(List::stream)
			.map(SectionEdge::of)
			.collect(Collectors.toList());

		sectionEdges.forEach(sectionEdge -> {
			graph.addEdge(sectionEdge.getSource(), sectionEdge.getTarget(), sectionEdge);
			graph.setEdgeWeight(sectionEdge, sectionEdge.getWeight());
		});
	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder(lines);
	}

	public Path find(Station source, Station target) {
		throwOnInvalid(source, target);
		ShortestPathAlgorithm<Station, SectionEdge> shortestPathAlgorithm = new DijkstraShortestPath<>(graph);
		GraphPath<Station, SectionEdge> graphPath = shortestPathAlgorithm.getPath(source, target);
		if (graphPath == null) {
			throw new CanNotFindPathException("출발역과 도착역이 연결되어 있지 않습니다.");
		}

		List<Station> stations = graphPath.getVertexList();
		List<Section> sections = SectionEdge.toSections(graphPath.getEdgeList());
		return Path.of(stations, sections, (int)graphPath.getWeight());
	}

	private void throwOnInvalid(Station source, Station target) {
		throwOnEqual(source, target);
		throwOnNotExist(source, target);
	}

	private void throwOnEqual(Station source, Station target) {
		if (source.equals(target)) {
			throw new CanNotFindPathException("출발역과 도착역은 동일할 수 없습니다.");
		}
	}

	private void throwOnNotExist(Station source, Station target) {
		if (!stations.contains(source)) {
			throw new CanNotFindPathException("존재하지 않는 출발역입니다.");
		}

		if (!stations.contains(target)) {
			throw new CanNotFindPathException("존재하지 않는 도착역입니다.");
		}
	}
}

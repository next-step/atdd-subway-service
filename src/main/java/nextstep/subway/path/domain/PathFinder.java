package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private PathFinder() {
	}

	public static Optional<Path> findPath(Sections sections, Station source, Station target) {
		throwExceptionIfEqual(source, target);

		if(isNotAllStationInLine(sections, Arrays.asList(source, target))) {
			return Optional.empty();
		}

		return Optional.ofNullable(findPathUsingJgrapht(sections, source, target))
			.map(graphPath -> new Path(graphPath.getVertexList(), Math.round(graphPath.getWeight())));
	}

	private static GraphPath<Station, DefaultWeightedEdge> findPathUsingJgrapht(Sections sections,
		Station source, Station target) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		sections.getStations()
			.stream()
			.distinct()
			.forEach(graph::addVertex);
		sections.getSections()
			.forEach(section -> addOrUpdateEdge(graph, section));
		return new DijkstraShortestPath<>(graph).getPath(source, target);
	}

	private static void throwExceptionIfEqual(Station source, Station target) {
		if(Objects.equals(source, target)){
			throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
		}
	}

	private static boolean isNotAllStationInLine(Sections sections, List<Station> stations) {
		long stationCountInLine = sections.getStations()
			.stream()
			.filter(stations::contains)
			.count();
		return stationCountInLine != stations.size();
	}

	private static void addOrUpdateEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
		DefaultWeightedEdge edge = graph.getEdge(section.getUpStation(), section.getDownStation());
		if (edge != null) {
			graph.setEdgeWeight(edge, Math.min(graph.getEdgeWeight(edge), section.getDistance()));
			return;
		}
		graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
	}
}

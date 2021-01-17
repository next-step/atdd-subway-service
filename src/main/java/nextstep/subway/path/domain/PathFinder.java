package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private PathFinder() {
	}

	public static Optional<Path> findPath(List<Line> lines, Station source, Station target) {
		throwExceptionIfEqual(source, target);

		if(isNotAllStationInLine(lines, Arrays.asList(source, target))) {
			return Optional.empty();
		}

		return Optional.ofNullable(findPathUsingJgrapht(lines, source, target))
			.map(graphPath -> new Path(graphPath.getVertexList(), Math.round(graphPath.getWeight())));
	}

	private static GraphPath<Station, DefaultWeightedEdge> findPathUsingJgrapht(List<Line> lines,
		Station source, Station target) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct().forEach(graph::addVertex);
		lines.stream()
			.flatMap(line -> line.getSections().stream())
			.forEach(section -> addOrUpdateEdge(graph, section));
		return new DijkstraShortestPath<>(graph).getPath(source, target);
	}

	private static void throwExceptionIfEqual(Station source, Station target) {
		if(Objects.equals(source, target)){
			throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
		}
	}

	private static boolean isNotAllStationInLine(List<Line> lines, List<Station> stations) {
		long stationCountInLine = lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
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

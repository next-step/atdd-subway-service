package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.SectionEdge;
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
			.map(graphPath -> new Path(
				new Sections(
					graphPath.getEdgeList()
						.stream()
						.map(SectionEdge::getSection)
						.collect(Collectors.toList())
				),
				graphPath.getVertexList(),
				Math.round(graphPath.getWeight())));
	}

	private static GraphPath<Station, SectionEdge> findPathUsingJgrapht(Sections sections,
		Station source, Station target) {
		WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
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

	private static void addOrUpdateEdge(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
		SectionEdge beforeEdge = graph.getEdge(section.getUpStation(), section.getDownStation());

		if(beforeEdge == null) {
			addEdge(graph, section);
			return;
		}

		double beforeDistance = graph.getEdgeWeight(beforeEdge);
		if(beforeDistance > section.getDistance()) {
			beforeEdge.setSection(section);
			graph.setEdgeWeight(beforeEdge, section.getDistance());
		}

		changeCheapLineIfSameDistance(beforeEdge, beforeDistance, section);
	}

	private static void addEdge(WeightedMultigraph<Station, SectionEdge> graph, Section section) {
		SectionEdge newEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
		newEdge.setSection(section);
		graph.setEdgeWeight(newEdge, section.getDistance());
	}

	private static void changeCheapLineIfSameDistance(SectionEdge beforeEdge, double beforeDistance, Section section) {
		Line newLine = section.getLine();
		if(beforeDistance == section.getDistance() && beforeEdge.getLineExtraFare() > newLine.getExtraFare()) {
			beforeEdge.setSection(section);
		}
	}

}

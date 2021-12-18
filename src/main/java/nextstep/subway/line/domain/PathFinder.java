package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
	public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target, List<Line> lines) {
		validateSourceAndTargetIsEqual(source, target);
		List<Section> sections = getSectionsDistinctFromLines(lines);
		validateSourceAndTargetIsExist(source, target, new Sections(sections));

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = makeGraphFromSections(graph, sections);
		return getPath(source, target, dijkstraShortestPath);
	}

	private void validateSourceAndTargetIsEqual(Station source, Station target) {
		if (source.equals(target)) {
			throw Exceptions.SOURCE_AND_TARGET_EQUAL.getException();
		}
	}

	private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target,
		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
		GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
		return Optional.ofNullable(path).orElseThrow(Exceptions.SOURCE_AND_TARGET_NOT_CONNECTED::getException);
	}

	private DijkstraShortestPath<Station, DefaultWeightedEdge> makeGraphFromSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
		for (Section section : sections) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
				section.getDistance());
		}

		return new DijkstraShortestPath<>(graph);
	}

	private void validateSourceAndTargetIsExist(Station source, Station target, Sections sections) {
		if (!sections.contains(source) || !sections.contains(target)) {
			throw Exceptions.SOURCE_OR_TARGET_NOT_EXIST.getException();
		}
	}

	private List<Section> getSectionsDistinctFromLines(List<Line> lines) {
		return lines.stream()
			.flatMap(line -> line.getSections().stream())
			.collect(Collectors.toList());
	}
}

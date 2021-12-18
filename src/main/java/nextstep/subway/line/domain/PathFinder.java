package nextstep.subway.line.domain;

import java.util.List;
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
		validate(source, target);

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		List<Section> sections = getSectionsDistinctFromLines(lines);
		
		for (Section section : sections) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
				section.getDistance());
		}

		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		return dijkstraShortestPath.getPath(source, target);
	}

	private void validate(Station source, Station target) {
		if (source.equals(target)) {
			throw Exceptions.SOURCE_AND_TARGET_EQUAL.getException();
		}
	}

	private List<Section> getSectionsDistinctFromLines(List<Line> lines) {
		return lines.stream()
			.flatMap(line -> line.getSections().stream())
			.collect(Collectors.toList());
	}
}

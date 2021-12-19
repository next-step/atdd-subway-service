package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
	public GraphPath<Station, DefaultWeightedEdge> findShortestPath(Station source, Station target, Sections sections) {
		validate(source, target, sections);

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = makeGraphFromSections(graph, sections.get());
		return getPath(source, target, dijkstraShortestPath);
	}

	private void validate(Station source, Station target, Sections sections) {
		if (source.equals(target)) {
			throw Exceptions.SOURCE_AND_TARGET_EQUAL.getException();
		}

		if (!sections.contains(source) || !sections.contains(target)) {
			throw Exceptions.SOURCE_OR_TARGET_NOT_EXIST.getException();
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
}

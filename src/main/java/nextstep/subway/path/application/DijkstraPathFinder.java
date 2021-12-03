package nextstep.subway.path.application;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

@Component
public class DijkstraPathFinder implements PathFinder {

	@Override
	public Path findShortestPath(List<Line> lines, Station departStation, Station arriveStation) {

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		lines.stream()
			.map(line -> line.getSections())
			.flatMap(sections -> sections.getSections().stream())
			.forEach(section -> {
				graph.addVertex(section.getUpStation());
				graph.addVertex(section.getDownStation());
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance());
			});

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath path = dijkstraShortestPath.getPath(departStation, arriveStation);
		return new Path((int)path.getWeight(), path.getVertexList());
	}
}

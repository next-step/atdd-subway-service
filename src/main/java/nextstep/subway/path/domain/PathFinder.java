package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {

	public ShortestPath findShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = this.findDijkstraShortestPath(lines);
		GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

		return new ShortestPath(path.getVertexList(), Math.round(path.getWeight()));
	}

	private DijkstraShortestPath findDijkstraShortestPath(List<Line> lines) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		List<Section> sections = new ArrayList<>();
		for(Line line : lines) {
			sections.addAll(line.getSections());
		}

		for(Section section : sections) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		}

		return new DijkstraShortestPath(graph);
	}
}

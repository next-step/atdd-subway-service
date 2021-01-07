package nextstep.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.TotalLines;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {

	public ShortestPath findShortestPath(TotalLines lines, Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = this.findDijkstraShortestPath(lines);
		GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

		if (path == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않은 경우, 최단 경로를 조회할 수 없습니다.");
		}

		return new ShortestPath(path.getVertexList(), Math.round(path.getWeight()));
	}

	private DijkstraShortestPath findDijkstraShortestPath(TotalLines lines) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for(Section section : lines.allSections()) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		}

		return new DijkstraShortestPath(graph);
	}
}

package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayMap {
	private List<Line> lines;

	protected SubwayMap() {
	}

	public SubwayMap(List<Line> lines) {
		this.lines = lines;
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = this.findDijkstraShortestPath();
		GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

		if (path == null) {
			throw new IllegalArgumentException("출발역과 도착역이 연결되어있지 않은 경우, 최단 경로를 조회할 수 없습니다.");
		}

		return new ShortestPath(path.getVertexList(), Math.round(path.getWeight()));
	}

	private DijkstraShortestPath findDijkstraShortestPath() {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for(Section section : allSections()) {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		}

		return new DijkstraShortestPath(graph);
	}

	public List<Section> allSections() {
		List<Section> sections = new ArrayList<>();
		lines.forEach(line -> sections.addAll(line.getSections()));
		return sections;
	}
}

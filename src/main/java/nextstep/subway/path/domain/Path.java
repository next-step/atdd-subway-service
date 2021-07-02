package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
	private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
	private final DijkstraShortestPath dijkstraShortestPath;
	private final Station source;
	private final Station target;

	public Path(List<Line> lines, Station source, Station target) {
		if(source.equals(target)) {
			throw new IllegalArgumentException("시작역과 종료역이 같습니다.");
		}

		addVertexes(lines);
		addEdgeWeights(lines);

		this.source = source;
		this.target = target;
		dijkstraShortestPath = new DijkstraShortestPath(graph);
	}

	private void addEdgeWeights(List<Line> lines) {
		lines.stream()
			.map(Line::getSections)
			.forEach(sections -> sections.setEdgeWeight(graph));
	}

	private void addVertexes(List<Line> lines) {
		lines.stream()
			.flatMap(line -> line.getStations().stream())
			.distinct()
			.forEach(station -> graph.addVertex(station));
	}

	public List<Station> getShortestStations() {
		return dijkstraShortestPath.getPath(source, target).getVertexList();
	}

	public int getShortestDistance() {
		return (int) dijkstraShortestPath.getPath(source, target).getWeight();
	}
}

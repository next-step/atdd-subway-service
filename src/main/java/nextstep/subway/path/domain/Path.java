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

	public Path(List<Line> lines) {
		addVertexes(lines);
		addEdgeWeights(lines);

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

	public List<Station> getShortestStations(Station source, Station target) {
		return dijkstraShortestPath.getPath(source, target).getVertexList();
	}

	public int getShortestDistance(Station source, Station target) {
		return (int) dijkstraShortestPath.getPath(source, target).getWeight();
	}
}

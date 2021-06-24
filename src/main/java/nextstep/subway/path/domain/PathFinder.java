package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {

	private final WeightedMultigraph<Station, DefaultWeightedEdge> pathGraph;
	private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

	public PathFinder(List<Line> lines) {
		this.pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		for (Line line : lines) {
			this.initVertex(line);
			this.initEdge(line);
		}
		shortestPath = new DijkstraShortestPath<>(this.pathGraph);
	}

	private void initEdge(Line line) {
		line.getSections()
			.getSections()
			.forEach(section -> {
				Station sourceVertex = section.getUpStation();
				Station targetVertex = section.getDownStation();
				int weight = section.getDistance().getDistance();
				this.pathGraph.setEdgeWeight(
					this.pathGraph.addEdge(sourceVertex, targetVertex), weight);
			});
	}

	private void initVertex(Line line) {
		List<Station> stations = line.getStations();
		stations.forEach(this.pathGraph::addVertex);
	}

	public Path getShortestPath(Station source, Station target) {
		GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
		return new Path(path.getVertexList(), path.getWeight());
	}
}

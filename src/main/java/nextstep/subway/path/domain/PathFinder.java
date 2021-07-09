package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	private DijkstraShortestPath dijkstraShortestPath;;

	public PathFinder(List<Line> lines) {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		
		for (Line line : lines) {
			for (Station station : line.getStations()) {
				graph.addVertex(station.getName());
			}

			for (Section section : line.getSection()) {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
			}
		}

		this.dijkstraShortestPath = new DijkstraShortestPath(graph);
	}

	public List<String> findPath(Station startStation, Station destinationStation) {
		return dijkstraShortestPath.getPath(startStation.getName(), destinationStation.getName()).getVertexList();
	}

	public int findPathLength(Station startStation, Station destinationStation) {
		return (int) dijkstraShortestPath.getPathWeight(startStation.getName(), destinationStation.getName());
	}
}

package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
	private DijkstraShortestPath dijkstraShortestPath;
	private WeightedMultigraph<String, DefaultWeightedEdge> graph;

	public PathFinder(List<Line> lines) {
		graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for (Line line : lines) {
			addStations(graph, line);
			setLengthBetweenTwoStation(graph, line);
		}

		this.dijkstraShortestPath = new DijkstraShortestPath(graph);
	}

	private void setLengthBetweenTwoStation(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
		for (Section section : line.getSection()) {
			graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
		}
	}

	private void addStations(WeightedMultigraph<String, DefaultWeightedEdge> graph, Line line) {
		for (Station station : line.getStations()) {
			graph.addVertex(station.getName());
		}
	}

	public List<String> findPath(Station startStation, Station destinationStation) {
		validateStartStationAndDestinationStationAreSame(startStation, destinationStation);
		validateStationReachable(startStation, destinationStation);

		return dijkstraShortestPath.getPath(startStation.getName(), destinationStation.getName()).getVertexList();
	}

	private void validateStationReachable(Station startStation, Station destinationStation) {
		if (!graph.containsVertex(startStation.getName()) || !graph.containsVertex(destinationStation.getName())) {
			throw new RuntimeException("Start Station or destination station can not be reached");
		}
	}

	private void validateStartStationAndDestinationStationAreSame(Station startStation, Station destinationStation) {
		if(startStation.equals(destinationStation)) {
			throw new RuntimeException("Start station and Destination station can not be same");
		}
	}

	public int findPathLength(Station startStation, Station destinationStation) {
		return (int) dijkstraShortestPath.getPathWeight(startStation.getName(), destinationStation.getName());
	}
}

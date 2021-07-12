package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@Component
public class PathFinder {
	private static final int BASE_FARE = 1250;
	private static final int FIRTST_STANDARD_ADDITIONAL_FARE_LENGTH = 5;
	private static final int ADDITIONAL_FARE = 100;
	private static final int BASE_LENGTH = 10;
	public static final int SECOND_STANDARD_ADDITIONAL_FARE_LENGTH = 8;

	private DijkstraShortestPath dijkstraShortestPath;
	private WeightedMultigraph<String, DefaultWeightedEdge> graph;

	public PathFinder(List<Line> lines) {
		this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for (Line line : lines) {
			line.setStationsGraph(graph);
		}

		this.dijkstraShortestPath = new DijkstraShortestPath(graph);
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

	public int getFare(Station startStation, Station destinationStation) {
		if (findPathLength(startStation, destinationStation) > 50) {
			return BASE_FARE + calculateOverFare(40, FIRTST_STANDARD_ADDITIONAL_FARE_LENGTH) + calculateOverFare(findPathLength(startStation, destinationStation) - 50, SECOND_STANDARD_ADDITIONAL_FARE_LENGTH);
		}

		if (findPathLength(startStation, destinationStation) > 10) {
			return BASE_FARE + calculateOverFare(findPathLength(startStation, destinationStation) - BASE_LENGTH, FIRTST_STANDARD_ADDITIONAL_FARE_LENGTH);
		}

		return BASE_FARE;
	}

	private int calculateOverFare(int distance, int standardLength) {
		return (int) ((Math.ceil((distance - 1) / standardLength) + 1) * ADDITIONAL_FARE);
	}
}

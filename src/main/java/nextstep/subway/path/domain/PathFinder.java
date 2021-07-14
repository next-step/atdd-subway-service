package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinder {
	// private static final int BASE_FARE = 1250;
	// private static final int FIRST_STANDARD_ADDITIONAL_FARE_LENGTH = 5;
	// private static final int ADDITIONAL_FARE = 100;
	// private static final int BASE_LENGTH = 10;
	// private static final int SECOND_STANDARD_ADDITIONAL_FARE_LENGTH = 8;

	private DijkstraShortestPath dijkstraShortestPath;
	private WeightedMultigraph<String, AdditionalFareEdge> graph;
	private LoginMember loginMember;

	public PathFinder(List<Line> lines, LoginMember loginMember) {
		this.loginMember = loginMember;
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
		FarePolicy farePolicy = getFarePolicy(startStation, destinationStation);

		return farePolicy.getFare(loginMember);
	}

	private FarePolicy getFarePolicy(Station startStation, Station destinationStation) {
		return FarePolicyFactory.getFarePolicy(dijkstraShortestPath.getPath(startStation.getName(), destinationStation.getName()).getEdgeList(), findPathLength(startStation, destinationStation));

	}

	// private int getFareOnLength(Station startStation, Station destinationStation) {
	// 	if (findPathLength(startStation, destinationStation) > 50) {
	// 		return BASE_FARE + calculateOverFare(40, FIRST_STANDARD_ADDITIONAL_FARE_LENGTH) + calculateOverFare(findPathLength(startStation, destinationStation) - 50, SECOND_STANDARD_ADDITIONAL_FARE_LENGTH) + getAdditionalFare(startStation, destinationStation);
	// 	}
	//
	// 	if (findPathLength(startStation, destinationStation) > 10) {
	// 		return BASE_FARE + calculateOverFare(findPathLength(startStation, destinationStation) - BASE_LENGTH,
	// 			FIRST_STANDARD_ADDITIONAL_FARE_LENGTH) + getAdditionalFare(startStation, destinationStation);
	// 	}
	//
	// 	return BASE_FARE + getAdditionalFare(startStation, destinationStation);
	// }
	//
	// private int calculateOverFare(int distance, int standardLength) {
	// 	return (int) ((Math.ceil((distance - 1) / standardLength) + 1) * ADDITIONAL_FARE);
	// }
	//
	// private int getAdditionalFare(Station startStation, Station destinationStation) {
	// 	return dijkstraShortestPath.getPath(startStation.getName(), destinationStation.getName()).getEdgeList().stream()
	// 		.mapToInt(x -> ((AdditionalFareEdge)x).getAdditionalFare())
	// 		.max()
	// 		.orElse(0);
	// }
}

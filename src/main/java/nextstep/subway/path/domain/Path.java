package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

	private static final Fare DEFAULT_FARE = new Fare(1250);
	private static final Fare FARE_PER_DISTANCE = new Fare(100);
	private static final Fare FARE_FREE = new Fare(0);
	private static final Distance DISTANCE_50KM = new Distance(50);
	private static final Distance DISTANCE_10KM = new Distance(10);
	private static final Distance DISTANCE_8KM = new Distance(8);
	private static final Distance DISTANCE_5KM = new Distance(5);
	private static final Distance DISTANCE_ZERO = new Distance(0);

	private final GraphPath<Station, LineEdge> path;

	public Path(GraphPath<Station, LineEdge> path) {
		this.path = path;
	}

	public List<Station> getStations() {
		return path.getVertexList();
	}

	public Fare getFare(int age) {
		final Fare allFare = DEFAULT_FARE.plus(getDistanceFare());
		final Fare discount = DiscountPolicy.find(age).calculateDiscount(allFare);
		return allFare.minus(discount);
	}

	private Fare getDistanceFare() {
		Distance pathDistance = getDistance();
		if (pathDistance.isGreaterThan(DISTANCE_50KM)) {
			int multiplier = pathDistance.floorDiv(DISTANCE_8KM);
			return FARE_PER_DISTANCE.multiply(multiplier);
		}

		if (pathDistance.isGreaterThan(DISTANCE_10KM)) {
			int multiplier = pathDistance.floorDiv(DISTANCE_5KM);
			return FARE_PER_DISTANCE.multiply(multiplier);
		}

		return FARE_FREE;
	}

	public Distance getDistance() {
		return path.getEdgeList().stream()
				.map(LineEdge::getDistance)
				.reduce(Distance::plus)
				.orElse(DISTANCE_ZERO);
	}
}

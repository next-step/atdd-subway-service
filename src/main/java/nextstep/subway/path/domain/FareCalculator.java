package nextstep.subway.path.domain;

public class FareCalculator {
	private final int totalDistance;
	private final DistanceFareGrade distanceFareGrade;

	public FareCalculator(int totalDistance, DistanceFareGrade distanceFareGrade) {
		this.totalDistance = totalDistance;
		this.distanceFareGrade = distanceFareGrade;
	}

	public static FareCalculator of(Path path) {
		int totalDistance = path.sumTotalDistance();
		DistanceFareGrade distanceFareGrade = DistanceFareGrade.of(totalDistance);
		return new FareCalculator(totalDistance, distanceFareGrade);
	}

	public Fare calculateFare() {
		return distanceFareGrade.calculateFare(totalDistance);
	}
}

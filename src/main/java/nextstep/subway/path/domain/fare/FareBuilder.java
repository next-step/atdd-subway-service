package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Distance;

public class FareBuilder {

	private final Distance distance;

	public FareBuilder(int distance) {
		this.distance = new Distance(distance);
	}

	public Money calculate() {
		return DistanceCalculator.apply(distance);
	}
}

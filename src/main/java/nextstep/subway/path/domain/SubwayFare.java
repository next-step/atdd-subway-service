package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Objects;

import nextstep.subway.line.domain.Distance;

public class SubwayFare {
	private static final BigDecimal SUBWAY_BASE_FARE = new BigDecimal(1250);
	private final BigDecimal subwayFare;

	private SubwayFare() {
		subwayFare = SUBWAY_BASE_FARE;
	}

	private SubwayFare(double overFare) {
		this.subwayFare = new BigDecimal(overFare).add(SUBWAY_BASE_FARE);
	}

	public int value() {
		return subwayFare.intValue();
	}

	public static SubwayFare calculate(Distance distance) {
		if (distance.lessThanOrEqual(Distance.of(10))) {
			return new SubwayFare();
		}

		Distance overDistance = distance.decrease(Distance.of(10));

		if (distance.lessThanOrEqual(Distance.of(50))) {
			return new SubwayFare(calculateOverFareTenToFifty(overDistance));
		}

		return new SubwayFare(calculateOverFareMoreThenFifty(overDistance));
	}

	private static double calculateOverFareTenToFifty(Distance overDistance) {
		return Math.ceil(overDistance.divide(5)) * 100;
	}

	private static double calculateOverFareMoreThenFifty(Distance overDistance) {
		Distance tenToFiftyDistance = Distance.of(40);
		Distance moreThenFiftyDistance = overDistance.decrease(tenToFiftyDistance);
		return (Math.ceil(moreThenFiftyDistance.divide(8)) * 100)
				+ calculateOverFareTenToFifty(tenToFiftyDistance);
	}
}

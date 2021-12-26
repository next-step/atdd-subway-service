package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public class DistanceChargeCalculator {

	private DistanceChargeCalculator() {
	}

	public static Fare getFare(Distance distance) {
		Fare fare = Fare.of();
		for (DistanceChargePolicy policy : DistanceChargePolicy.values()) {
			fare = fare.add(calculateFare(policy, distance));
		}
		return fare;
	}

	private static Fare calculateFare(DistanceChargePolicy policy, Distance distance) {
		if (distance.isLessThan(policy.getMinimumKm())) {
			return Fare.zero();
		}
		if (distance.isGreaterThan(policy.getMaxKm())) {
			distance = Distance.of(policy.getMaxKm());
		}
		Distance overDistance = distance.getOverDistance(policy.getMinimumKm());
		return Fare.of(calculateOverFare(policy, overDistance));
	}

	private static int calculateOverFare(DistanceChargePolicy policy, Distance distance) {
		int distanceNumber = distance.toInt();
		return (((distanceNumber - 1) / policy.getEveryKm()) + 1) * policy.getSurcharge();
	}
}

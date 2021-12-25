package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public enum DistanceChargePolicy {

	FIRST_CHARGE_POLICY(11, 50, 5, 100),
	SECOND_CHARGE_POLICY(51, Integer.MAX_VALUE, 8, 100);

	private final int minimumKm;
	private final int maxKm;
	private final int everyKm;
	private final int surcharge;

	DistanceChargePolicy(int minKm, int maxKm, int everyKm, int surcharge) {
		this.minimumKm = minKm;
		this.maxKm = maxKm;
		this.everyKm = everyKm;
		this.surcharge = surcharge;
	}

	public static Fare getFare(Distance distance) {
		Fare fare = Fare.of();
		for (DistanceChargePolicy policy : DistanceChargePolicy.values()) {
			fare = fare.add(policy.calculateFare(distance));
		}
		return fare;
	}

	private Fare calculateFare(Distance distance) {
		if (distance.isLessThan(this.minimumKm)) {
			return Fare.zero();
		}
		if (distance.isGreaterThan(this.maxKm)) {
			distance = Distance.of(this.maxKm);
		}
		Distance overDistance = distance.getOverDistance(this.minimumKm);
		return Fare.of(calculateOverFare(overDistance));
	}

	private int calculateOverFare(Distance distance) {
		int distanceNumber = distance.toInt();
		return (((distanceNumber - 1) / this.everyKm) + 1) * this.surcharge;
	}
}

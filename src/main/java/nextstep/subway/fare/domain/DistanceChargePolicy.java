package nextstep.subway.fare.domain;

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

	public static Fare getFare(int distance) {
		Fare fare = Fare.of();
		for (DistanceChargePolicy policy : DistanceChargePolicy.values()) {
			fare = fare.add(policy.calculateFare(distance));
		}
		return fare;
	}

	private Fare calculateFare(int distance) {
		if (distance < this.minimumKm) {
			return Fare.of();
		}
		if (distance > this.maxKm) {
			distance = this.maxKm;
		}
		return Fare.of(calculateOverFare(distance - (this.minimumKm - 1)));
	}

	private int calculateOverFare(int distance) {
		return (((distance - 1) / this.everyKm) + 1) * this.surcharge;
	}
}

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

	public static int getFare(int distance) {
		int fare = 0;
		for (DistanceChargePolicy policy : DistanceChargePolicy.values()) {
			fare += policy.getEachFare(distance);
		}
		return fare;
	}

	private int getEachFare(int distance) {
		if (distance < this.minimumKm) {
			return 0;
		}
		if (distance > this.maxKm) {
			distance = this.maxKm;
		}
		return calculateOverFare(distance - (this.minimumKm - 1));
	}

	private int calculateOverFare(int distance) {
		return (((distance - 1) / this.everyKm) + 1) * this.surcharge;
	}
}

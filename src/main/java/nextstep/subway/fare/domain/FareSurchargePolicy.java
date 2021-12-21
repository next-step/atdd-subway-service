package nextstep.subway.fare.domain;

public enum FareSurchargePolicy {

	FIRST_SURCHARGE_POLICY(11, 50, 5, 100),
	SECOND_SURCHARGE_POLICY(51, Integer.MAX_VALUE, 8, 100);

	private static final int BASE_FARE = 1_250;

	private final int minimumKm;
	private final int maxKm;
	private final int everyKm;
	private final int surchargeFee;

	FareSurchargePolicy(int minKm, int maxKm, int everyKm, int surchargeFee) {
		this.minimumKm = minKm;
		this.maxKm = maxKm;
		this.everyKm = everyKm;
		this.surchargeFee = surchargeFee;
	}

	public static int getFare(int distance) {
		int fare = BASE_FARE;
		for (FareSurchargePolicy policy : FareSurchargePolicy.values()) {
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
		return (int)((Math.ceil((distance - 1) / this.everyKm) + 1) * this.surchargeFee);
	}
}

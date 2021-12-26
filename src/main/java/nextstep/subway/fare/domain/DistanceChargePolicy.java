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

	public int getMinimumKm() {
		return minimumKm;
	}

	public int getMaxKm() {
		return maxKm;
	}

	public int getEveryKm() {
		return everyKm;
	}

	public int getSurcharge() {
		return surcharge;
	}
}

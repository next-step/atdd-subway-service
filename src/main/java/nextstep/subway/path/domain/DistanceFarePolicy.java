package nextstep.subway.path.domain;

public enum DistanceFarePolicy {
	LTE_10KM(10, 1, 0),
	LTE_50KM(50, 5, 100),
	MAX(99999, 8, 100);

	private static int DEFAULT_DISTANCE_FARE = 1250;

	private int standardDistance;
	private int distanceUnit;
	private int additionalFare;

	DistanceFarePolicy(int standardDistance, int distanceUnit, int additionalFare) {
		this.standardDistance = standardDistance;
		this.distanceUnit = distanceUnit;
		this.additionalFare = additionalFare;
	}

	public static DistanceFarePolicy calculateDistanceFare(int distance) {
		for (DistanceFarePolicy farePolicy : DistanceFarePolicy.values()) {
			if (distance < farePolicy.getStandardDistance()) {
				return farePolicy;
			}
		}
		return DistanceFarePolicy.LTE_10KM;
	}

	public static int calculateDistanceFare(DistanceFarePolicy policy, int distance) {
		return DEFAULT_DISTANCE_FARE + (int) ((Math.ceil((distance - 1) / policy.getDistanceUnit()) + 1) * policy.getAdditionalFare());
	}

	public int getStandardDistance() {
		return standardDistance;
	}

	public int getDistanceUnit() {
		return distanceUnit;
	}

	public int getAdditionalFare() {
		return additionalFare;
	}

	//	5km 마다 100원 추가 로직
	private int calculateOverFare(int distance) {
		return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
	}
}

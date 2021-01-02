package nextstep.subway.path.domain;

import java.util.List;

public class FareCalculator {
	public static final int BASIC_FARE = 1250;
	public static final int BASIC_DISTANCE = 10;
	public static final int EXTRA_DISTANCE = 50;
	private int distance;
	private List<DistanceFarePolicy> distancePolicys;
	private AgeGroup ageGroup;
	private Integer extraFare;

	public FareCalculator(int distance, Integer age, Integer extraFare) {
		this.distance = distance;
		this.ageGroup = AgeGroup.findAgeGroup(age);
		this.distancePolicys = DistanceFarePolicy.findDistanceGroup(distance);
		this.extraFare = extraFare;
	}

	public int calculate() {
		int result = BASIC_FARE;
		for (DistanceFarePolicy distanceFarePolicy : distancePolicys) {
			result += calculateOverFare(distanceFarePolicy);
		}
		result += extraFare;
		return ageGroup.discountFare(result);
	}

	private int calculateOverFare(DistanceFarePolicy policy) {
		int overDistance = policy.calculateOverDistance(this.distance);
		return (int)((Math.ceil((overDistance) / (double)policy.getUnitDistance())) * policy.getUnitFare());
	}
}

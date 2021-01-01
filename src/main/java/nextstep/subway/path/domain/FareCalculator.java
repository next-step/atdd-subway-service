package nextstep.subway.path.domain;

public class FareCalculator {
	public static final int BASIC_FARE = 1250;
	public static final int BASIC_DISTANCE = 10;
	public static final int EXTRA_DISTANCE = 50;
	private int distance;
	private AgeGroup ageGroup;
	private Integer extraFare;

	public FareCalculator(int distance, Integer age, Integer extraFare) {
		this.distance = distance;
		this.ageGroup = AgeGroup.findAgeGroup(age);
		this.extraFare = extraFare;
	}

	public int calculate() {
		int result = BASIC_FARE;
		result += calculateOver10Fare();
		result += calculateOver50Fare();
		result += extraFare;
		return ageGroup.discountFare(result);
	}

	private int calculateOver10Fare() {
		if (distance <= BASIC_DISTANCE) {
			return 0;
		}
		int overDistance = distance - BASIC_DISTANCE;
		if (distance > EXTRA_DISTANCE) {
			overDistance = EXTRA_DISTANCE - BASIC_DISTANCE;
		}
		return (int)((Math.ceil((overDistance) / (double)5)) * 100);
	}

	private int calculateOver50Fare() {
		if (distance <= EXTRA_DISTANCE) {
			return 0;
		}
		int overDistance = distance - EXTRA_DISTANCE;
		return (int)((Math.ceil((overDistance) / (double)8)) * 100);
	}

}

package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgePolicy {
	CHILD(6, 12, 0.5),
	ADOLESCENT(13, 18, 0.2),
	ADULT(19, Integer.MAX_VALUE, 1.0);

	private int minAge;
	private int maxAge;

	private double discountRate;

	AgePolicy(int minAge, int maxAge, double discountRate) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.discountRate = discountRate;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public static AgePolicy getAgePolicyByAge(int age) {
		return Arrays.stream(AgePolicy.values())
			.filter(agePolicy -> agePolicy.minAge <= age && agePolicy.maxAge > age)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}

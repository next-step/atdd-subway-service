package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum AgeDiscountRule {
	CHILDREN (0, 13, 350, 0.5f),
	TEENAGER (13, 19, 350, 0.8f),
	BASIC(19, null, 0, 1f);


	private int minAge;
	private Optional<Integer> maxAge;
	private int discountFare;
	private float discountFareRate;

	AgeDiscountRule(int minAge, Integer maxAge, int discountFare, float discountFareRate) {
		this.minAge = minAge;
		this.maxAge = Optional.ofNullable(maxAge);
		this.discountFare = discountFare;
		this.discountFareRate = discountFareRate;
	}

	public int calculateFare(int fare) {
		return (int) ((fare - discountFare) * discountFareRate);
	}

	public static AgeDiscountRule valueOfAge(int age) {
		return Arrays.stream(AgeDiscountRule.values())
			.filter(fare -> fare.isBetweenMinAndMax(age))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요금 정책에 맞지 않은 나이 입니다."));
	}

	private boolean isBetweenMinAndMax(int age) {
		int maxAge = findMaxAge(age);
		return age > this.minAge && age <= maxAge;
	}

	private int findMaxAge(int age) {
		int maxAge = age;
		if (this.maxAge.isPresent()) {
			maxAge = this.maxAge.get();
		}
		return maxAge;
	}
}

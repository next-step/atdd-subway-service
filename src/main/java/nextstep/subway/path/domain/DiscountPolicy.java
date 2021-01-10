package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;

import java.util.Arrays;
import java.util.function.IntPredicate;

enum DiscountPolicy {
	CHILDREN(0.5, age -> age >= 6 && age < 13),
	TEENAGER(0.2, age -> age >= 13 && age < 19),
	STANDARD(0, age -> false);

	private static final Fare DEDUCT_FARE = new Fare(350);
	private double rate;
	private IntPredicate ageCondition;

	DiscountPolicy(double rate, IntPredicate ageCondition) {
		this.rate = rate;
		this.ageCondition = ageCondition;
	}

	public Fare calculateDiscount(Fare allFare) {
		return allFare.minus(DEDUCT_FARE).multiply(this.rate);
	}

	public static DiscountPolicy find(int age) {
		return Arrays.stream(values())
				.filter(discountPolicy -> discountPolicy.isInclude(age))
				.findFirst()
				.orElse(STANDARD);
	}

	private boolean isInclude(int age) {
		return this.ageCondition.test(age);
	}
}

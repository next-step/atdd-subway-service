package nextstep.subway.member.domain;

import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;

public enum AgeDiscountPolicy {

	TODDLER(age -> age < 6, totalFare -> 0),
	KIDS(age -> age >= 6 && age < 13, totalFare -> discount(totalFare, Rate.KIDS_DISCOUNT_RATE)),
	TEENAGER(age -> age >= 13 && age < 19, totalFare -> discount(totalFare, Rate.TEENAGER_DISCOUNT_RATE)),
	ADULT(age -> age >= 19, totalFare -> totalFare);

	private static final int FIX_DISCOUNT_FARE = 350;

	private static class Rate {
		private static final double KIDS_DISCOUNT_RATE = 0.5;
		private static final double TEENAGER_DISCOUNT_RATE = 0.8;
	}

	private static int discount(Integer totalFare, double discountRate) {
		return (int)Math.ceil((totalFare - FIX_DISCOUNT_FARE) * discountRate);
	}

	private final IntPredicate condition;
	private final UnaryOperator<Integer> expression;

	AgeDiscountPolicy(IntPredicate condition, UnaryOperator<Integer> expression) {
		this.condition = condition;
		this.expression = expression;
	}

	public static int discountFare(LoginMember member, int totalFare) {
		if (member.isGuest()) {
			return totalFare;
		}
		AgeDiscountPolicy ageDiscountPolicy = valueOf(member.age());
		return ageDiscountPolicy.expression.apply(totalFare);
	}

	private static AgeDiscountPolicy valueOf(Age age) {
		return java.util.Arrays.stream(values())
			.filter(category -> category.condition.test(age.value()))
			.findFirst()
			.orElse(ADULT);
	}

	public int discount(int totalFare) {
		return this.expression.apply(totalFare);
	}

}

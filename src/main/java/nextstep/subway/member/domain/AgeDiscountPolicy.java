package nextstep.subway.member.domain;

import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Fare;

public enum AgeDiscountPolicy {

	TODDLER(age -> age < 6, totalFare -> Fare.zero()),
	KIDS(age -> age >= 6 && age < 13, totalFare -> discount(totalFare, Rate.KIDS_DISCOUNT_RATE)),
	TEENAGER(age -> age >= 13 && age < 19, totalFare -> discount(totalFare, Rate.TEENAGER_DISCOUNT_RATE)),
	ADULT(age -> age >= 19, totalFare -> totalFare);

	private static final Fare FIX_DISCOUNT_FARE = Fare.from(350);

	private static class Rate {
		private static final double KIDS_DISCOUNT_RATE = 0.5;
		private static final double TEENAGER_DISCOUNT_RATE = 0.2;
	}
	private final IntPredicate matcher;
	private final UnaryOperator<Fare> expression;

	AgeDiscountPolicy(IntPredicate matcher, UnaryOperator<Fare> expression) {
		this.matcher = matcher;
		this.expression = expression;
	}

	public static Fare discountFare(LoginMember member, Fare totalFare) {
		if (member.isGuest()) {
			return totalFare;
		}
		AgeDiscountPolicy ageDiscountPolicy = valueOf(member.age());
		return ageDiscountPolicy.expression.apply(totalFare);
	}

	private static Fare discount(Fare totalFare, double discountRate) {
		return totalFare.subtract(FIX_DISCOUNT_FARE)
			.percentOf(1 - discountRate);
	}

	private static AgeDiscountPolicy valueOf(Age age) {
		return java.util.Arrays.stream(values())
			.filter(category -> category.matcher.test(age.value()))
			.findFirst()
			.orElse(ADULT);
	}
}

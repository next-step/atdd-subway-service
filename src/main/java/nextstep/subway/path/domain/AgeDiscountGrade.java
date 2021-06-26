package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.Fare.*;

import java.util.stream.Stream;

import nextstep.subway.line.domain.Fare;

enum AgeDiscountGrade {
	TODDLER(1, 6, originFare -> ZERO),
	CHILDREN(6, 13, originFare -> originFare.minus(Fare.wonOf(350)).times(0.5)),
	STUDENT(13, 19, originFare -> originFare.minus(Fare.wonOf(350)).times(0.8)),
	ADULT(19, 100, originFare -> originFare);

	private final int startAgeInclusive;
	private final int endAgeExclusive;
	private final DiscountStrategy discountStrategy;

	AgeDiscountGrade(int startAgeInclusive, int endAgeExclusive, DiscountStrategy discountStrategy) {
		this.startAgeInclusive = startAgeInclusive;
		this.endAgeExclusive = endAgeExclusive;
		this.discountStrategy = discountStrategy;
	}

	public static AgeDiscountGrade of(int age) {
		return Stream.of(AgeDiscountGrade.values())
			.filter(ageDiscountGrade -> ageDiscountGrade.matchAge(age))
			.findAny()
			.orElseThrow(IllegalStateException::new);
	}

	Fare discountFare(Fare originFare) {
		return discountStrategy.discountFare(originFare);
	}

	private boolean matchAge(int age) {
		return startAgeInclusive <= age && age < endAgeExclusive;
	}
}

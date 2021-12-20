package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

import nextstep.subway.fare.exception.FareNotFoundException;

public enum AgeFare {

	DEFAULT(age -> false, 0.f),
	KID(age -> (6 <= age && age < 13), 0.5f),
	TEENAGER(age -> (13 <= age && age < 19), 0.2f);

	private final Function<Integer, Boolean> condition;
	private final float discountRate;

	AgeFare(Function<Integer, Boolean> condition, float discountRate) {
		this.condition = condition;
		this.discountRate = discountRate;
	}

	private static final int FIXED_DISCOUNT_KRW = 350;

	public static Fare calculate(int age, Fare generalFare) {
		validate(generalFare);
		final AgeFare ageFare = getAgeFare(age);
		if (AgeFare.DEFAULT == ageFare) {
			return generalFare;
		}
		return Fare.of(calculate(generalFare.getFare(), ageFare.discountRate));
	}

	private static void validate(Fare fare) {
		if (null == fare) {
			throw new FareNotFoundException();
		}
	}

	private static AgeFare getAgeFare(int age) {
		return Arrays.stream(values())
			.filter(ageFare -> ageFare.condition.apply(age))
			.findAny()
			.orElse(DEFAULT);
	}

	private static int calculate(int generalFareKRW, float discountRate) {
		return (Math.round((generalFareKRW - FIXED_DISCOUNT_KRW) * (1.f - discountRate)));
	}
}

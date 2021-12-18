package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.FareNotFoundException;

public class AgeFare {

	private static final int KID_MIN_AGE_INCLUSIVE = 6;
	private static final int KID_MAX_AGE_EXCLUSIVE = 13;
	private static final double KID_DISCOUNT_RATE = 0.5f;

	private static final int TEENAGER_MIN_AGE_INCLUSIVE = 13;
	private static final int TEENAGER_MAX_AGE_EXCLUSIVE = 19;
	private static final double TEENAGER_DISCOUNT_RATE = 0.2f;

	private static final int FIXED_DISCOUNT_KRW = 350;

	public static Fare calculate(int age, Fare generalFare) {
		validate(generalFare);
		if (isKid(age)) {
			return Fare.of(kid(generalFare.getFare()));
		} else if (isTeenager(age)) {
			return Fare.of(teenager(generalFare.getFare()));
		}
		return generalFare;
	}

	private static void validate(Fare fare) {
		if (null == fare) {
			throw new FareNotFoundException();
		}
	}

	private static int kid(int generalFareKRW) {
		return (int) (Math.round((generalFareKRW - FIXED_DISCOUNT_KRW) * (1.f - KID_DISCOUNT_RATE)));
	}

	private static int teenager(int generalFareKRW) {
		return (int) (Math.round((generalFareKRW - FIXED_DISCOUNT_KRW) * (1.f - TEENAGER_DISCOUNT_RATE)));
	}

	private static boolean isTeenager(int age) {
		return TEENAGER_MIN_AGE_INCLUSIVE <= age && age < TEENAGER_MAX_AGE_EXCLUSIVE;
	}

	private static boolean isKid(int age) {
		return KID_MIN_AGE_INCLUSIVE <= age && age < KID_MAX_AGE_EXCLUSIVE;
	}
}

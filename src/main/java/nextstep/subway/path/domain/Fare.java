package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

public class Fare {
	private static final int DEFAULT_FARE = 1250;
	private static final int BASIC_DISCOUNT_FARE_FOR_NON_ADULT_USER = 350;
	private static final int ADDITIONAL_FARE_PER_SECTION = 100;
	private static final double CHILD_DISCOUNT_RATE = 0.5;
	private static final double TEENAGER_DISCOUNT_RATE = 0.8;
	private static final int LONG_DISTANCE_FOR_EXTRA_FARE = 8;
	private static final int MIDDLE_DISTANCE_FOR_EXTRA_FARE = 5;
	private int fare;

	private Fare(final int fare) {
		this.fare = fare;
	}

	public static Fare totalFareOf(final Distance totalDistance, final LoginMember loginMember, final int lineSurcharge) {
		int totalFare = DEFAULT_FARE + lineSurcharge;
		totalFare += calculateFareWithLongDistance(totalDistance);
		totalFare += calculateFareWithMiddleDistance(totalDistance);
		if (loginMember.isLoginUser()) {
			return new Fare(calculateDiscountFare(loginMember, totalFare));
		}
		return new Fare(totalFare);
	}

	private static int calculateFareWithLongDistance(final Distance totalDistance) {
		if (totalDistance.isLongDistance()) {
			return totalDistance.getLongDistance() / LONG_DISTANCE_FOR_EXTRA_FARE * ADDITIONAL_FARE_PER_SECTION;
		}
		return 0;
	}

	private static int calculateFareWithMiddleDistance(final Distance totalDistance) {
		if (totalDistance.hasMiddleDistance()) {
			return totalDistance.getMiddleDistance() / MIDDLE_DISTANCE_FOR_EXTRA_FARE * ADDITIONAL_FARE_PER_SECTION;
		}
		return 0;
	}

	private static int calculateDiscountFare(final LoginMember loginMember,final int totalFare) {
		if (loginMember.isBaby()) {
			return 0;
		}
		if (loginMember.isChild()) {
			return calculateDiscountFareForLoginUser(totalFare, CHILD_DISCOUNT_RATE);
		}
		if (loginMember.isTeenager()) {
			return calculateDiscountFareForLoginUser(totalFare, TEENAGER_DISCOUNT_RATE);
		}
		return totalFare;
	}

	private static int calculateDiscountFareForLoginUser(final int totalFare, final double childDiscountRate) {
		return (int) ((totalFare - BASIC_DISCOUNT_FARE_FOR_NON_ADULT_USER) * childDiscountRate);
	}

	public int getTotalFare() {
		return this.fare;
	}
}

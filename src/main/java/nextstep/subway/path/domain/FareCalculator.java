package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;

public class FareCalculator {

	private FareCalculator() {}

	public static Fare calculateFare(Path path, LoginMember loginMember) {
		Fare fare = path.calculatePathFare();
		if (loginMember.isAnonymous()) {
			return fare;
		}
		return discountFare(loginMember, fare);
	}

	private static Fare discountFare(LoginMember loginMember, Fare originFare) {
		AgeDiscountGrade ageDiscountGrade = AgeDiscountGrade.of(loginMember.getAge());
		return ageDiscountGrade.discountFare(originFare);
	}
}

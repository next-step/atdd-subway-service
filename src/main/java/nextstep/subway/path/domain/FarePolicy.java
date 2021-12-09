package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;

public class FarePolicy {
	private static final int BASIC_FARE = 1250;

	public int calculateBy(int distance) {
		DistanceBasedFarePolicy policy = new PerOverchargeFareSectionDistanceBasedFarePolicy();
		return policy.calculate(BASIC_FARE, distance);
	}

	public int calculateBy(int distance, List<Line> lines) {
		LineOverchargeFarePolicy policy = new MostExpensiveLineOverchargeFarePolicy();
		int fare = calculateBy(distance);
		return policy.overcharge(fare, lines);
	}

	public int calculateBy(int distance, List<Line> lines, LoginMember loginMember) {
		MemberDiscountFarePolicy policy = new PerAgeMemberDiscountFarePolicy();
		int fare = calculateBy(distance, lines);
		return policy.discount(fare, loginMember);
	}
}

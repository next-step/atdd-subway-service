package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;

public class FareCalculator {
	private static final int BASIC_FARE = 1250;

	private final DistanceBasedFarePolicy distanceBasedFarePolicy;
	private final LineOverchargeFarePolicy lineOverchargeFarePolicy;
	private final MemberDiscountFarePolicy memberDiscountFarePolicy;

	public FareCalculator(
		DistanceBasedFarePolicy distanceBasedFarePolicy,
		LineOverchargeFarePolicy lineOverchargeFarePolicy,
		MemberDiscountFarePolicy memberDiscountFarePolicy
	) {
		this.distanceBasedFarePolicy = distanceBasedFarePolicy;
		this.lineOverchargeFarePolicy = lineOverchargeFarePolicy;
		this.memberDiscountFarePolicy = memberDiscountFarePolicy;
	}

	public int calculate(int distance, List<Line> lines, LoginMember loginMember) {
		int fare = distanceBasedFarePolicy.calculate(BASIC_FARE, distance);
		int overchargeFare = lineOverchargeFarePolicy.overcharge(fare, lines);
		return memberDiscountFarePolicy.discount(overchargeFare, loginMember);
	}
}

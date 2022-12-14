package nextstep.subway.path.domain;

import static org.apache.commons.lang3.ObjectUtils.*;

import org.springframework.util.Assert;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.AgeDiscountPolicy;

public class TotalFareCalculator {

	private final LoginMember member;
	private final Distance distance;
	private final Sections sections;

	TotalFareCalculator(LoginMember member, Distance distance, Sections sections) {
		Assert.notNull(member, "사용자는 필수입니다.");
		Assert.notNull(distance, "거리는 필수입니다.");
		Assert.isTrue(isNotEmpty(sections), "구간들은 필수입니다.");
		this.member = member;
		this.distance = distance;
		this.sections = sections;
	}

	public static TotalFareCalculator of(LoginMember member, Distance distance, Sections sections) {
		return new TotalFareCalculator(member, distance, sections);
	}

	public Fare fare() {
		Fare fareWithoutDiscount = fareWithoutDiscount();
		return AgeDiscountPolicy.discountFare(member, fareWithoutDiscount);
	}

	private Fare fareWithoutDiscount() {
		Fare distanceFare = DistanceFarePolicy.calculate(distance);
		return distanceFare.sum(sections.maxExtraFare());
	}
}

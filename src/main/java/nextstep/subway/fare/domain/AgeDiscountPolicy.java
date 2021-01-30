package nextstep.subway.fare.domain;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;

@Component
public class AgeDiscountPolicy implements DiscountPolicy {
	@Override
	public Fare discount(Fare fare, LoginMember member) {
		return Fare.from(discountFareByAge(fare.getFare(), member.getAge()));
	}

	private long discountFareByAge(long fare, int age) {
		return Arrays.stream(AgeDiscountStandard.values())
			.filter(standard -> standard.equalOrMore <= age && age < standard.less)
			.findAny()
			.map(standard -> (long)((fare - standard.deduction) * standard.discountRate))
			.orElse(fare);
	}
}

package nextstep.subway.fare.domain;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
public class AgeDiscountPolicy implements DiscountPolicy{
	@Override
	public Fare discount(Fare fare, LoginMember member, Path path) {
		return Fare.from(discountFareByAge(fare.getFare(), member.getAge()));
	}

	public static long discountFareByAge(long fare, int age) {
		if (age <= 0 || age >= 20) {
			return fare;
		}
		if (age >= 13) {
			return (fare - 350) * 4 / 5;
		}
		if (age >= 6) {
			return (fare - 350) / 2;
		}
		return 0;
	}

}

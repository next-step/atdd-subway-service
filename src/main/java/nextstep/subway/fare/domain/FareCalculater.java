package nextstep.subway.fare.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
public class FareCalculater {

	private final List<FarePolicy> farePolicies;
	private final List<DiscountPolicy> discountPolicies;

	public FareCalculater(List<FarePolicy> farePolicies, List<DiscountPolicy> discountPolicies) {
		this.farePolicies = farePolicies;
		this.discountPolicies = discountPolicies;
	}

	public Fare calculateFare(LoginMember member, Path path) {
		Fare fare = farePolicies.stream()
			.map(policy -> policy.calculate(member, path))
			.reduce(Fare::sum)
			.orElseGet(() -> Fare.from(0));

		for(DiscountPolicy discountPolicy : discountPolicies) {
			fare = discountPolicy.discount(fare, member, path);
		}

		return fare;
	}
}

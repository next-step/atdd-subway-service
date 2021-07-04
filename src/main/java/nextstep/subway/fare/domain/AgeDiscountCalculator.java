package nextstep.subway.fare.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgeDiscountCalculator {

	private List<AgeDiscountPolicy> ageDiscountPolicies;
	private static AgeDiscountCalculator instance;

	private AgeDiscountCalculator() {
		ageDiscountPolicies = new ArrayList<>();
		ageDiscountPolicies.add(new ChildDiscountPolicy());
		ageDiscountPolicies.add(new TeenagerDiscountPolicy());
	}

	public static AgeDiscountCalculator getInstance() {
		if (Objects.isNull(instance)) {
			instance = new AgeDiscountCalculator();
		}
		return instance;
	}

	public Fare discount(Fare fare, int age) {
		return getAgeDiscountPolicy(age).discount(fare);
	}

	private AgeDiscountPolicy getAgeDiscountPolicy(int age) {
		return ageDiscountPolicies.stream()
			.filter(policy -> policy.isAccepted(age))
			.findFirst()
			.orElseGet(NotAgeDiscountPolicy::new);
	}

}

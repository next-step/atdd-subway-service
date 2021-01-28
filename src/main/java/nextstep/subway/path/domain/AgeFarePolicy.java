package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public enum AgeFarePolicy {
	DEFAULT(0, 0, fare -> fare),
	CHILD(6, 13, fare -> (int)((fare - 350) * 0.5)),
	TEENAGER(13, 19, fare -> (int)((fare - 350) * 0.8));

	private final int minAge;
	private final int maxAge;
	private final IntUnaryOperator policy;

	AgeFarePolicy(int minAge, int maxAge, IntUnaryOperator policy) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.policy = policy;
	}

	public int calculateDiscountedFare(int fare) {
		return policy.applyAsInt(fare);
	}

	private boolean isTargetAge(int age) {
		return age >= minAge && age < maxAge;
	}

	public static AgeFarePolicy find(int age) {
		return Arrays.stream(AgeFarePolicy.values())
			.filter(ageFarePolicy -> ageFarePolicy.isTargetAge(age))
			.findFirst()
			.orElse(DEFAULT);
	}
}

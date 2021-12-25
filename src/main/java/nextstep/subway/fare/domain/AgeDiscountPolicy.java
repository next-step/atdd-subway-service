package nextstep.subway.fare.domain;

import java.util.Arrays;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.member.domain.Age;

public enum AgeDiscountPolicy {

	YOUTH_DISCOUNT_POLICY(13, 18, 20),
	CHILDREN_DISCOUNT_POLICY(6, 12, 50),
	NOT_DISCOUNT_POLICY(19, Integer.MAX_VALUE, 0);

	private final int minAge;
	private final int maxAge;
	private final int percent;

	AgeDiscountPolicy(int minAge, int maxAge, int percent) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.percent = percent;
	}

	public static int discountByAge(int fare, Age age) {
		AgeDiscountPolicy policy = findPolicyByAge(age);
		return (int)((100 - policy.percent) / 100D * fare);
	}

	private static AgeDiscountPolicy findPolicyByAge(Age age) {

		return Arrays.stream(AgeDiscountPolicy.values())
			.filter(policy -> age.isEqualsAndBetween(policy.minAge, policy.maxAge))
			.findFirst()
			.orElseThrow(() ->
				new AppException(ErrorCode.INTERNAL_SERVER_ERROR,
					"해당하는 나이(age:{})에 할인정책이 없습니다", age));
	}

}

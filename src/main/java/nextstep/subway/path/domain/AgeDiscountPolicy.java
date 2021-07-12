package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.math.BigDecimal;
import java.util.Arrays;

public enum AgeDiscountPolicy {
	CHILDREN(6, 12, BigDecimal.valueOf(350), 0.5),
	TEENAGER(13, 18, BigDecimal.valueOf(350), 0.2),
	NO_DISCOUNT(0, 0, BigDecimal.ZERO, 0);

	private final int minAge;
	private final int maxAge;
	private final BigDecimal discountAmount;
	private final double discountPercent;

	AgeDiscountPolicy(int minAge, int maxAge, BigDecimal discountAmount, double discountPercent) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.discountAmount = discountAmount;
		this.discountPercent = discountPercent;
	}

	public static AgeDiscountPolicy valueByMatchedAge(LoginMember loginMember) {
		if (loginMember.getAge() == null) {
			return NO_DISCOUNT;
		}

		return Arrays.stream(AgeDiscountPolicy.values())
				.filter(ageDiscountPolicy -> ageDiscountPolicy.filterAge(loginMember.getAge()))
				.findFirst()
				.orElse(NO_DISCOUNT);
	}

	private boolean filterAge(int age) {
		return minAge <= age && age <= maxAge;
	}

	public BigDecimal apply(BigDecimal charge) {
		return calculateDiscountAmount(charge);
	}

	private BigDecimal calculateDiscountAmount(BigDecimal charge) {
		return charge.subtract(discountAmount)
				.multiply(BigDecimal.valueOf(1.0 - discountPercent));
	}
}

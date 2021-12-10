package nextstep.subway.path.domain.price;

import java.util.Arrays;
import java.util.Optional;

public enum DiscountPolicy {
	STUDENT(13, 19, 350, 0.8),
	KIDS(6, 13, 350, 0.5);

	private int startAge;
	private int exclusiveEndAge;
	private int deduction;
	private double adaptDiscountRate;

	DiscountPolicy(int startAge, int exclusiveEndAge, int deduction, double adaptDiscountRate) {
		this.startAge = startAge;
		this.exclusiveEndAge = exclusiveEndAge;
		this.deduction = deduction;
		this.adaptDiscountRate = adaptDiscountRate;
	}

	public int getDeduction() {
		return deduction;
	}

	public double getAdaptDiscountRate() {
		return adaptDiscountRate;
	}

	public static Optional<DiscountPolicy> findDiscountType(int age) {
		DiscountPolicy[] discountTypes = DiscountPolicy.values();

		return Arrays.stream(discountTypes)
			.filter(policy -> policy.isBelongTo(age))
			.findFirst();
	}

	private boolean isBelongTo(int age) {
		return startAge <= age && age < exclusiveEndAge;
	}
}
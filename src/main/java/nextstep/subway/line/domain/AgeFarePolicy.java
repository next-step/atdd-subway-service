package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeFarePolicy {
	CHILD(350, 0.5, age -> 6 <= age && age < 13),
	STUDENT(350, 0.8, age -> 13 <= age && age < 19);

	private final int deductionFare;
	private final double inverseDiscountRate;
	private final Function<Integer, Boolean> ageType;

	AgeFarePolicy(final int deductionFare, final double inverseDiscountRate,
		final Function<Integer, Boolean> ageType) {
		this.deductionFare = deductionFare;
		this.inverseDiscountRate = inverseDiscountRate;
		this.ageType = ageType;
	}

	public static AgeFarePolicy valueOf(int age) {
		return Arrays.stream(AgeFarePolicy.values())
			.filter(it -> it.isAgeFarePolicy(age))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	public boolean isAgeFarePolicy(int age) {
		return ageType.apply(age);
	}

	public static Fare calculate(final int age, final Fare fare) {
		AgeFarePolicy ageFarePolicy = AgeFarePolicy.valueOf(age);
		return fare.minus(ageFarePolicy.deductionFare)
			.multiply(ageFarePolicy.inverseDiscountRate);
	}
}

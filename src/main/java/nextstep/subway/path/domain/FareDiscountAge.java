package nextstep.subway.path.domain;

import java.util.Arrays;

public enum FareDiscountAge {
	CHILDREN(6, 13, 350, 0.5f),
	TEENAGER(13, 19, 350, 0.2f);

	private int start;
	private int end;
	private int deductAmount;
	private float discountRate;

	FareDiscountAge(int start, int end, int deductAmount, float discountRate) {
		this.start = start;
		this.end = end;
		this.deductAmount = deductAmount;
		this.discountRate = discountRate;
	}

	int getDiscountFare(int fare) {
		return (int)((fare - deductAmount) * (1 - discountRate));
	}

	static boolean contains(int age) {
		return findBy(age) != null;
	}

	static FareDiscountAge findBy(int age) {
		return Arrays.stream(values())
			.filter(a -> age >= a.start && age < a.end)
			.findAny()
			.orElse(null);
	}
}

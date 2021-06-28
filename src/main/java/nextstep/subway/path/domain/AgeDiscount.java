package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscount {
	INFANT(1, 5, fare -> 0),
	CHILDREN(6, 12, fare -> (int)((fare - 350) * 0.5)),
	TEENAGER(13, 18, fare -> (int)((fare - 350) * 0.8)),
	ADULT(19, 200, fare -> fare);

	private int startAge;
	private int endAge;
	private DiscountStrategy discountStrategy;

	AgeDiscount(int startAge, int endAge, DiscountStrategy discountStrategy) {
		this.startAge = startAge;
		this.endAge = endAge;
		this.discountStrategy = discountStrategy;
	}

	public int discountedFare(int fare) {
		return this.discountStrategy.discount(fare);
	}

	public static AgeDiscount findByAge(int age) {
		return Arrays.stream(AgeDiscount.values()).filter(ageDiscount -> ageDiscount.matches(age))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("가능한 나이 값을 초과했습니다."));
	}

	private boolean matches(int age) {
		return startAge <= age && endAge >= age;
	}
}

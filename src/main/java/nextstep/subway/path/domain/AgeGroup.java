package nextstep.subway.path.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
	ADULT("어른", 19, 1000, 0),
	YOUTH("청소년", 13, 18, 0.2),
	CHILDREN("어린이", 6, 12, 0.5),
	BABY("아기", 0, 5, 1);

	private final String name;
	private final int minAge;
	private final int maxAge;
	private final double discountRate;

	public static AgeGroup findAgeGroup(Integer age) {
		if (age == null) {
			return ADULT;
		}
		return Arrays.stream(AgeGroup.values())
			.filter(ageGroup -> ageGroup.getMinAge() <= age && ageGroup.getMaxAge() >= age)
			.findFirst()
			.orElse(ADULT);
	}

	public int discountFare(int result) {
		if (this.equals(ADULT)) {
			return result;
		}
		if (this.equals(BABY)) {
			return 0;
		}
		return result - (int)Math.round((result - FareCalculator.DEDUCTION_FARE) * this.discountRate);
	}
}

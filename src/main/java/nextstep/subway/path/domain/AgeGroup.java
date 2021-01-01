package nextstep.subway.path.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.NotFoundException;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
	ADULT("어른", 19, 1000, 0),
	YOUTH("청소년", 13, 18, 0.2),
	CHILDREN("어린이", 6, 12, 0.5),
	BABY("아기", 0, 5, 1);

	public static final int DEDUCTION_FARE = 350;
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
			.orElseThrow(() -> new NotFoundException("나이에 맞는 나이그룹을 찾을 수 없습니다."));
	}

	public int discountFare(int fare) {
		if (this.equals(ADULT)) {
			return fare;
		}
		if (this.equals(BABY)) {
			return 0;
		}
		return fare - (int)Math.round((fare - DEDUCTION_FARE) * this.discountRate);
	}
}

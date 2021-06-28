package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeDiscountTest {

	@DisplayName("6세 이하에 대해 할인 요금 적용")
	@Test
	void testInfantDiscount() {

		AgeDiscount ageDiscount = AgeDiscount.findByAge(3);
		int discountedFare = ageDiscount.getDiscountedFare(2000);

		Assertions.assertThat(ageDiscount).isEqualTo(AgeDiscount.INFANT);
		Assertions.assertThat(discountedFare).isEqualTo(0);
	}

	@DisplayName("어린이 할인 요금 적용 - 6 ~ 12세")
	@Test
	void testChildrenDiscount() {
		AgeDiscount ageDiscount = AgeDiscount.findByAge(9);
		int discountedFare = ageDiscount.getDiscountedFare(2000);

		Assertions.assertThat(ageDiscount).isEqualTo(AgeDiscount.CHILDREN);
		Assertions.assertThat(discountedFare).isEqualTo(825);
	}

	@DisplayName("청소년 할인 요금 적용 - 13 ~ 18세")
	@Test
	void testTeenagerDiscount() {
		AgeDiscount ageDiscount = AgeDiscount.findByAge(15);
		int discountedFare = ageDiscount.getDiscountedFare(2000);

		Assertions.assertThat(ageDiscount).isEqualTo(AgeDiscount.TEENAGER);
		Assertions.assertThat(discountedFare).isEqualTo(1320);
	}

	@DisplayName("Enumeration 범위 밖의 나이 입력시 오류")
	@Test
	void testInvalidAge() {
		Assertions.assertThatThrownBy(() -> {
			AgeDiscount.findByAge(400);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가능한 나이 값을 초과했습니다.");
	}

}
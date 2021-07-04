package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeDiscountPolicyTest {

	@DisplayName("어린이 정책 할인 테스트")
	@Test
	void 어린이_정책_할인_테스트() {
		assertThat(AgeDiscountCalculator.getInstance().discount(new Fare(1000), 10).value()).isEqualTo(675);
	}

	@DisplayName("청소년 정책 할인 테스트")
	@Test
	void 청소_정책_할인_테스트() {
		assertThat(AgeDiscountCalculator.getInstance().discount(new Fare(1000), 15).value()).isEqualTo(870);
	}

	@DisplayName("성인 정책 할인 테스트 할인 안됌")
	@Test
	void 성인_정책_할인_테스트_할인_안됌() {
		assertThat(AgeDiscountCalculator.getInstance().discount(new Fare(1000), 20).value()).isEqualTo(1000);
	}
}

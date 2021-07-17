package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.AgePolicy.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgePolicyTest {

	@DisplayName("어린이인 경우, 50%할인해준다.")
	@Test
	void 어린이_이용시() {
		AgePolicy agePolicy = getAgePolicyByAge(8);
		assertThat(agePolicy.getDiscountRate()).isEqualTo(0.5);
	}

	@DisplayName("청소년인 경우, 20%할인해준다.")
	@Test
	void 청소년_이용시() {
		AgePolicy agePolicy = getAgePolicyByAge(16);
		assertThat(agePolicy.getDiscountRate()).isEqualTo(0.2);
	}

	@DisplayName("성인인 경우, 할인이 없다.")
	@Test
	void 성인_이용시() {
		AgePolicy agePolicy = getAgePolicyByAge(21);
		assertThat(agePolicy.getDiscountRate()).isEqualTo(1.0);
	}

}

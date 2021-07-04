package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;

public class DistanceFarePolicyTest {

	@DisplayName("기본 요금 정책 테스트")
	@Test
	void 기본_요금_정책_테스트() {
		assertThat(DistanceFareCalculator.getInstance().calculate(new Distance(5)).value()).isEqualTo(1250);
	}

	@DisplayName("가까운 거리 요금 정책 테스트")
	@Test
	void 가까운_거리_요금_정책_테스트() {
		assertThat(DistanceFareCalculator.getInstance().calculate(new Distance(15)).value()).isEqualTo(1550);
	}

	@DisplayName("먼 거리 요금 정책 테스트")
	@Test
	void 먼_거리_요금_정책_테스트() {
		assertThat(DistanceFareCalculator.getInstance().calculate(new Distance(56)).value()).isEqualTo(1950);
	}
}

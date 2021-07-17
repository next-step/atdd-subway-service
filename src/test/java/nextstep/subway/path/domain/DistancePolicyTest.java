package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.DistancePolicy.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistancePolicyTest {

	@DisplayName("이동 거리가 10km이내인 경우, 요금은 1250원이다.")
	@Test
	void 이동_거리_10키로_이내() {
		assertThat(getFareByDistance(5)).isEqualTo(1250);
	}

	@DisplayName("성인이 추가요금이 없는 노선의 이동 거리가 10km초과 50km 이내인 경우, 5km마다 100원 추가 요금이 붙는다")
	@Test
	void 이동_거리_10키로_초과_50키로_이내() {
		int expectFare = 1250 + 100;
		assertThat(getFareByDistance(12)).isEqualTo(expectFare);
	}

	@DisplayName("성인이 추가요금이 없는 노선의 이동 거리가 50km초과인 경우, 8km마다 100원 추가 요금이 붙는다")
	@Test
	void 이동_거리_50키로_초과() {
		int expectFare = 1250 + 700;
		assertThat(getFareByDistance(66)).isEqualTo(expectFare);
	}

}

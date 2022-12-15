package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;

@DisplayName("추가요금 생성 테스트")
class DistanceFareFactoryTest {

	@DisplayName("기본 거리 요금 생성")
	@Test
	void defaultDistanceFareTest() {
		assertThat(DistanceFareFactory.defaultFare()).isEqualTo(Fare.from(1250));
	}

	@DisplayName("중간 거리 요금 생성")
	@ParameterizedTest(name = "[{index}] 거리: {0} | 요금: {1}")
	@CsvSource(value = {"11:1350", "20:1450", "30:1650", "40:1850", "50:2050"}, delimiter = ':')
	void mediumDistanceFareTest(int distance, int fare) {
		assertThat(DistanceFareFactory.mediumDistanceFare(Distance.from(distance))).isEqualTo(Fare.from(fare));
	}

	@DisplayName("중간 거리 기준 보다 작은 거리 입력 시 예외")
	@Test
	void mediumDistanceFareExceptionTest() {
		// given
		Distance distance = Distance.from(10);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> DistanceFareFactory.mediumDistanceFare(distance));
	}

	@DisplayName("긴 거리 요금 생성")
	@ParameterizedTest(name = "[{index}] 거리: {0} | 요금: {1}")
	@CsvSource(value = {"51:2150", "60:2250", "70:2350", "80:2450", "91:2650"}, delimiter = ':')
	void longDistanceFareTest(int distance, int fare) {
		assertThat(DistanceFareFactory.longDistanceFare(Distance.from(distance))).isEqualTo(Fare.from(fare));
	}

	@DisplayName("긴 거리 기준 보다 작은 거리 입력 시 예외")
	@Test
	void longDistanceFareExceptionTest() {
		// given
		Distance distance = Distance.from(50);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> DistanceFareFactory.longDistanceFare(distance));
	}
}
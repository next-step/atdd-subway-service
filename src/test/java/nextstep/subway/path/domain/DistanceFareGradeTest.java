package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리별 요금 계산 테스트")
class DistanceFareGradeTest {

	@DisplayName("거리가 1 ~ 10 사이이면 요금은 1250 이다.")
	@ParameterizedTest()
	@ValueSource(ints = {1, 2, 9, 10})
	void basicTest(int totalDistance) {
		DistanceFareGrade fareGrade = DistanceFareGrade.of(totalDistance);

		Fare fare = fareGrade.calculateFare(totalDistance);

		assertThat(fare).isEqualTo(Fare.wonOf(1250));
	}

	@DisplayName("거리가 11 ~ 50 사이 이면 기본요금에 5km 당 100 원이 추가된다.")
	@ParameterizedTest()
	@CsvSource(value = {"11:1350", "25:1550", "50:2050"}, delimiter = ':')
	void middleTest(int totalDistance, int expectedFare) {
		DistanceFareGrade fareGrade = DistanceFareGrade.of(totalDistance);

		Fare fare = fareGrade.calculateFare(totalDistance);

		assertThat(fare).isEqualTo(Fare.wonOf(expectedFare));
	}

	@DisplayName("거리가 51 이상 이면 기본요금에 8km 당 100 원이 추가된다.")
	@ParameterizedTest()
	@CsvSource(value = {"51:2150", "129:3050", "171:3650"}, delimiter = ':')
	void longTest(int totalDistance, int expectedFare) {
		DistanceFareGrade fareGrade = DistanceFareGrade.of(totalDistance);

		Fare fare = fareGrade.calculateFare(totalDistance);

		assertThat(fare).isEqualTo(Fare.wonOf(expectedFare));
	}

}
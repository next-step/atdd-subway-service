package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.line.domain.Fare;

@DisplayName("거리별 추가 요금 계산 테스트")
class OverDistanceFareGradeTest {

	@DisplayName("거리가 1 ~ 10 사이이면 추가 요금은 0 이다.")
	@ParameterizedTest()
	@ValueSource(ints = {1, 2, 9, 10})
	void basicTest(int totalDistance) {
		OverDistanceFareGrade fareGrade = OverDistanceFareGrade.of(totalDistance);

		Fare overFare = fareGrade.calculateOverFare(totalDistance);

		assertThat(overFare).isEqualTo(Fare.wonOf(0));
	}

	@DisplayName("거리가 11 ~ 50 사이이면 5km 당 100 원이 추가된다.")
	@ParameterizedTest()
	@CsvSource(value = {"11:100", "25:300", "50:800"}, delimiter = ':')
	void middleTest(int totalDistance, int expectedFare) {
		OverDistanceFareGrade fareGrade = OverDistanceFareGrade.of(totalDistance);

		Fare fare = fareGrade.calculateOverFare(totalDistance);

		assertThat(fare).isEqualTo(Fare.wonOf(expectedFare));
	}

	@DisplayName("거리가 51 이상이면 8km 당 100 원이 추가된다.")
	@ParameterizedTest()
	@CsvSource(value = {"51:900", "129:1800", "171:2400"}, delimiter = ':')
	void longTest(int totalDistance, int expectedFare) {
		OverDistanceFareGrade fareGrade = OverDistanceFareGrade.of(totalDistance);

		Fare fare = fareGrade.calculateOverFare(totalDistance);

		assertThat(fare).isEqualTo(Fare.wonOf(expectedFare));
	}

}
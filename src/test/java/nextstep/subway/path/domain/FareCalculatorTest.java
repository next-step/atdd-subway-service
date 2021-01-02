package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {
	public static final Integer NO_LOGIN = null;
	public static final int 추가요금_없음 = 0;

	@DisplayName("거리별 요금 정책 : 10km초과∼50km까지(5km마다 100원), 50km초과 시 (8km마다 100원)")
	@ParameterizedTest
	@CsvSource({
		"1,1250",
		"10,1250",
		"11,1350",
		"15,1350",
		"16,1450",
		"20,1450",
		"21,1550",
		"25,1550",
		"26,1650",
		"30,1650",
		"31,1750",
		"35,1750",
		"36,1850",
		"40,1850",
		"41,1950",
		"45,1950",
		"46,2050",
		"50,2050",
		"51,2150",
		"58,2150",
		"59,2250",
		"66,2250",
		"67,2350",
		"74,2350",
	})
	void basicFare(int distance, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, NO_LOGIN, 추가요금_없음);

		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
	}

	@DisplayName("추가 요금 정책 : 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
	@ParameterizedTest
	@CsvSource({
		"1,0,1250",
		"15,900,2250",
		"20,1500,2950",
		"51,3000,5150"
	})
	void extraFare(int distance, int extraFare, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, NO_LOGIN, extraFare);
		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
	}

	@DisplayName("연령별 요금 할인 적용: 청소년: 운임에서 350원을 공제한 금액의 20%할인, 어린이: 6세 이상~ 13세 미만")
	@ParameterizedTest
	@CsvSource({
		"1,5,0",
		"1,6,800",
		"1,12,800",
		"1,13,1070",
		"1,18,1070",
		"1,19,1250",
		"1,100,1250",

		"11,5,0",
		"11,6,850",
		"11,12,850",
		"11,13,1150",
		"11,18,1150",
		"11,19,1350",
		"11,100,1350",

		"74,5,0",
		"74,6,1350",
		"74,12,1350",
		"74,13,1950",
		"74,18,1950",
		"74,19,2350",
		"74,100,2350",
	})
	void ageDiscount(int distance, int age, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, age, 추가요금_없음);
		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
	}

}
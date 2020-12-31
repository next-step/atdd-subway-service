package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FeeCalculatorTest {

	public static final Integer NO_LOGIN = null;

	@DisplayName("기본 운임 테스트")
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
	void basicFee(int distance, int expectedFee) {
		FeeCalculator feeCalculator = new FeeCalculator(distance, NO_LOGIN, 0);
		int result = feeCalculator.calculate();
		assertThat(result).isEqualTo(expectedFee);
	}

	@DisplayName("추가 요금")
	@ParameterizedTest
	@CsvSource({
		"1,0,1250",
		"10,500,1750",
		"15,1000,2350",
		"20,1000,2450",
		"51,2000,4150"
	})
	void extraFee(int distance, int extraFee, int expectedFee) {
		FeeCalculator feeCalculator = new FeeCalculator(distance, NO_LOGIN, extraFee);
		int result = feeCalculator.calculate();
		assertThat(result).isEqualTo(expectedFee);
	}

	@DisplayName("연령별 요금 할인 적용")
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
	void ageDiscount(int distance, int age, int expectedFee) {
		FeeCalculator feeCalculator = new FeeCalculator(distance, age, 0);
		int result = feeCalculator.calculate();
		assertThat(result).isEqualTo(expectedFee);
	}

}
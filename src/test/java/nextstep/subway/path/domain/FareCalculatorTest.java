package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;

class FareCalculatorTest {
	public static final Integer NO_LOGIN = null;

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;

	@BeforeEach
	void setup() {
		강남역 = new Station(1L, "강남역");
		양재역 = new Station(2L, "양재역");
		교대역 = new Station(3L, "교대역");
		신분당선 = LineResponse.of(new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 900));
		이호선 = LineResponse.of(new Line("이호선", "bg-red-600", 교대역, 강남역, 10, 500));
		삼호선 = LineResponse.of(new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 0));
	}

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
	void basicFare(int distance, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, NO_LOGIN, Collections.singletonList(삼호선));

		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
	}

	@DisplayName("추가 요금")
	@ParameterizedTest
	@CsvSource({
		"1,2150",
		"15,2250",
		"20,2350",
		"51,3050"
	})
	void extraFare(int distance, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, NO_LOGIN, Arrays.asList(이호선, 삼호선, 신분당선));
		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
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
	void ageDiscount(int distance, int age, int expectedFare) {
		FareCalculator fareCalculator = new FareCalculator(distance, age, Collections.singletonList(삼호선));
		int result = fareCalculator.calculate();
		assertThat(result).isEqualTo(expectedFare);
	}

}
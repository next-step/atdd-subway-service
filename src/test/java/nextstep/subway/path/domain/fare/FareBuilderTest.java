package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;

class FareBuilderTest {

	/**
	 *
	 * @param age : 나이
	 * @param fare : 나이별 요금
	 */
	@DisplayName("2. 나이별 요금 생성 테스트(58키로 기본요금 2,150)")
	@ParameterizedTest
	@CsvSource(value = {"2:0", "9:900", "16:1440", "30:2150", "70:0"}, delimiter = ':')
	void createAgeFareTest(int age, int fare) {
		// when // given
		Distance distance = new Distance(58);
		FareAge fareAge = FareAge.findFareAge(age);

		// then
		assertThat(FareBuilder.calculateDistance(distance, fareAge)).isEqualTo(new Money(fare));
	}

	/**
	 * 요금체계 : http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354
	 * @param km: 거리
	 * @param fare: 요금
	 */
	@DisplayName("1. 거리별 요금 생성 테스트")
	@ParameterizedTest
	@CsvSource(value = {"10:1250", "15:1350", "18:1450", "20:1450", "50:2050", "55:2150", "58:2150", "59:2250"},
		delimiter = ':')
	void createFareTest(int km, int fare) {
		// when // given
		Distance distance = new Distance(km);
		FareAge fareAge = FareAge.findFareAge(30);

		// then
		assertThat(FareBuilder.calculateDistance(distance, fareAge)).isEqualTo(new Money(fare));
	}
}

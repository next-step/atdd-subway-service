package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;

class FareBuilderTest {

	/**
	 * 요금체계 : http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354
	 * @param km: 거리
	 * @param fare: 요금
	 */
	@DisplayName("1. 거리별 요금 생성 테스트")
	@ParameterizedTest
	@CsvSource(value = {"10:1250", "15:1350", "20:1450", "50:2050", "58:2150"}, delimiter = ':')
	void createFareTest(int km, int fare) {
		// when // given
		Distance distance = new Distance(km);

		// then
		assertThat(FareBuilder.calculateDistance(distance)).isEqualTo(new Money(fare));
	}
}

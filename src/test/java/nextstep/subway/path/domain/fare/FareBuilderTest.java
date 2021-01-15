package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareBuilderTest {

	/**
	 * 요금체계 : http://www.seoulmetro.co.kr/kr/page.do?menuIdx=354
	 * @param distance: 거리
	 * @param fare: 요금
	 */
	@DisplayName("1. 거리별 요금 생성 테스트")
	@ParameterizedTest
	@CsvSource(value = {"10:1250", "15:1550", "20:1650", "58:2050"}, delimiter = ':')
	void createFareTest(int distance, int fare) {
		// when // given
		FareBuilder fareBuilder = new FareBuilder(distance);

		// then
		assertThat(fareBuilder.calculate()).isEqualTo(new Money(fare));
	}
}

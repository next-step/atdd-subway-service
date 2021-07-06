package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Lines 클래스 기능 검증 테스트
 */
@DisplayName("Lines 클래스 기능 검증 테스트")
class LinesTest {

	@Test
	@DisplayName("최종 추가요금 확인")
	void maxSurcharge() {
		// given
		Line green = new Line("2호선", "green", 900);
		Line orange = new Line("3호선", "orange", 0);
		Line blue = new Line("4호선", "blue", 800);
		Lines lines = new Lines(Arrays.asList(green, orange, blue));

		// when
		int finalSurcharge = lines.getFinalSurcharge();

		// then
		assertThat(finalSurcharge).isEqualTo(900);
	}
}

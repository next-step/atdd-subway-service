package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 간격 값 객체 테스트")
public class DistanceTest {

	@Test
	void 구간_간격_생성() {
		// when
		Distance 구간_간격 = new Distance(10);

		// then
		assertThat(구간_간격).isNotNull();
	}

	@Test
	void 구간_간격_생성_0이하의_간격_오류_발생() {
		// when

		// then
		assertThatThrownBy(() -> new Distance(0)).isInstanceOf(RuntimeException.class);
	}

	@Test
	void 구간_간격_마이너스_계산() {
		// given
		Distance 구간_간격 = new Distance(10);
		int 새로운_구간_간격 = 5;

		// when
		int 계산된_구간_간격 = 구간_간격.minus(새로운_구간_간격);

		// then
		assertThat(계산된_구간_간격).isEqualTo(구간_간격.value() - 새로운_구간_간격);
	}

	@Test
	void 구간_간격_마이너스_계산_결과값_0이하_오류_발생() {
		// given
		Distance 구간_간격 = new Distance(10);
		int 새로운_구간_간격 = 10;

		// when

		// then
		assertThatThrownBy(() -> 구간_간격.minus(새로운_구간_간격)).isInstanceOf(RuntimeException.class);
	}
}

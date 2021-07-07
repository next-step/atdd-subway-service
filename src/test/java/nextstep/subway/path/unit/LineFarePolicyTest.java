package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.LineFarePolicy;

public class LineFarePolicyTest {

	@DisplayName("노선별 요금 정책 검증")
	@CsvSource({"100,400,200", "400,0,10", "200,0,6000"})
	@ParameterizedTest
	void fare(int a, int b, int c) {
		List<Line> lines = Arrays.asList(
			new Line("name1", "color1", a, null, null, 1),
			new Line("name2", "color2", b, null, null, 1),
			new Line("name3", "color3", c, null, null, 1)
		);
		LineFarePolicy lineFarePolicy = new LineFarePolicy(lines);
		int max = Math.max(Math.max(a, b), c);

		assertThat(lineFarePolicy.fare()).isEqualTo(max);
	}
}

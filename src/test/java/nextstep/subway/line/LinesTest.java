package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;

@DisplayName("노선들 도메인 테스트")
public class LinesTest {

	@DisplayName("노선들 생성 테스트")
	@Test
	void createTest() {
		// given
		Lines expected = Lines.of(Arrays.asList(LineTest.일호선, LineTest.이호선));

		// when
		Lines lines = Lines.of(Arrays.asList(LineTest.일호선, LineTest.이호선));

		// then
		assertThat(lines).isEqualTo(expected);
	}

	@DisplayName("역들 중에 가장 요금이 높은 노선의 요금을 찾는다")
	@Test
	void findMostExpensiveLineFare() {
		// given
		Line 일호선 = Line.of(1L, "일호선", "red", StationTest.노포역, StationTest.서면역, 10, 900);
		Line 이호선 = Line.of(2L, "이호선", "blue", StationTest.서면역, StationTest.수영역, 9, 1000);
		Line 삼호선 = Line.of(3L, "삼호선", "green", StationTest.서면역, StationTest.수영역, 9, 2000);
		Lines lines = Lines.of(Arrays.asList(일호선, 이호선, 삼호선));

		List<Station> stations = Arrays.asList(StationTest.노포역, StationTest.서면역, StationTest.수영역);

		// when
		int result = lines.findMostExpensiveLineFare(stations);

		// then
		assertThat(result).isEqualTo(2000);
	}
}

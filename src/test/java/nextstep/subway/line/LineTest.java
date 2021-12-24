package nextstep.subway.line;

import static nextstep.subway.line.SectionTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DisplayName("노선 도메인 테스트")
public class LineTest {

	@DisplayName("노선을 생성한다")
	@Test
	void createTest() {
		// given
		Line expected = Line.of(1L, "1호선", "red", SECTION_1.getUpStation(), SECTION_1.getDownStation(), 10, 900);

		// when
		Line line = Line.of(1L, "1호선", "red", SECTION_1.getUpStation(), SECTION_1.getDownStation(), 10, 900);

		// then
		assertThat(line).isEqualTo(expected);

	}

	@DisplayName("정렬된 역들을 조회한다")
	@Test
	void getStationsTest() {
		// given
		Line line = Line.of(1L, "1호선", "red", SECTION_1.getUpStation(), SECTION_1.getDownStation(), 10, 1000);

		// when
		List<Station> stations = line.getStations();

		// then
		assertThat(stations).containsExactly(SECTION_1.getUpStation(), SECTION_1.getDownStation());
	}
}

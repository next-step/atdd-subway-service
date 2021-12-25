package nextstep.subway.line;

import static nextstep.subway.line.SectionTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Stations;

@DisplayName("노선 도메인 테스트")
public class LineTest {

	public static Line 일호선 = Line.of(1L, "1호선", "red", StationTest.노포역, StationTest.서면역, 10, 900);
	public static Line 이호선 = Line.of(2L, "2호선", "yellow", StationTest.전포역, StationTest.서면역, 1, 100);
	public static Line 삼호선 = Line.of(2L, "3호선", "yellow", StationTest.해운대역, StationTest.수영역, 1, 1100);

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
		Stations stations = line.getStations();

		// then
		assertThat(stations.containsAll(SECTION_1.getUpStation(), SECTION_1.getDownStation())).isTrue();
	}

	@DisplayName("노선 요금을 조회한다")
	@Test
	void getFareTest() {
		// given
		Line line = Line.of(1L, "1호선", "red", SECTION_1.getUpStation(), SECTION_1.getDownStation(), 10, 900);

		// when
		Fare result = line.getFare();

		// then
		assertThat(result).isEqualTo(Fare.of(900));
	}

	@DisplayName("역들 중 하나라도 포함되어 있는 구간이 있는지 체크한다")
	@Test
	void containsAnySectionTest() {
		// given
		Stations stations = Stations.of(Arrays.asList(StationTest.서면역, StationTest.노포역));
		Line line = Line.of(1L, "1호선", "red", StationTest.노포역, StationTest.서면역, 5, 900);

		// when
		boolean result = line.containsAnySection(stations);

		// then
		assertThat(result).isTrue();
	}
}

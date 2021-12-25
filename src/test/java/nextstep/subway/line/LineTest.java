package nextstep.subway.line;

import static nextstep.subway.line.SectionTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;

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
		List<Station> stations = line.getStations();

		// then
		assertThat(stations).containsExactly(SECTION_1.getUpStation(), SECTION_1.getDownStation());
	}

	@DisplayName("노선 요금을 조회한다")
	@Test
	void getFareTest() {
		// given
		Line line = Line.of(1L, "1호선", "red", SECTION_1.getUpStation(), SECTION_1.getDownStation(), 10, 900);

		// when
		int result = line.getFare();

		// then
		assertThat(result).isEqualTo(900);
	}

	@DisplayName("역들이 주어지면, 역들 중 하나라도 포함되어 있는지 체크한다")
	@Test
	void containsAnyStationTest() {
		// given
		List<Station> stations = new ArrayList<>();
		stations.add(StationTest.노포역);
		stations.add(StationTest.수영역);
		Line line = Line.of(1L, "1호선", "red", StationTest.노포역, StationTest.서면역, 5, 900);

		// when
		boolean result = line.containsAnyStation(stations);

		// then
		assertThat(result).isTrue();
	}
}

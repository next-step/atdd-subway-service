package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.AppException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinderTest {

	Section 노포역_서면역;
	Section 서면역_다대포역;
	Section 노포역_범내골역;
	Section 범내골역_다대포역;
	Sections sections;
	PathFinder pathFinder;

	@BeforeEach
	public void setup() {
		Line 일호선 = Line.of(1L, "1호선", "red", 900);
		노포역_서면역 = Section.of(1L, 일호선, StationTest.노포역, StationTest.서면역, 10);

		Line 이호선 = Line.of(2L, "2호선", "red", 1000);
		서면역_다대포역 = Section.of(2L, 이호선, StationTest.서면역, StationTest.다대포해수욕장역, 10);

		Line 삼호선 = Line.of(3L, "3호선", "red", 1100);
		노포역_범내골역 = Section.of(3L, 삼호선, StationTest.노포역, StationTest.범내골역, 3);
		범내골역_다대포역 = Section.of(4L, 삼호선, StationTest.범내골역, StationTest.다대포해수욕장역, 2);

		sections = Sections.of(Arrays.asList(노포역_서면역, 서면역_다대포역, 노포역_범내골역, 범내골역_다대포역));
		pathFinder = PathFinder.of(sections);
	}

	@DisplayName("경로를 찾는다")
	@Test
	void findPath() {
		// when
		PathResponse pathResponse = pathFinder.findPath(StationTest.다대포해수욕장역, StationTest.노포역);

		// then
		List<Station> stations = Arrays.asList(StationTest.다대포해수욕장역, StationTest.범내골역, StationTest.노포역);
		경로_확인(pathResponse, stations);
	}

	@DisplayName("경로를 찾으면 총 거리를 반환한다")
	@Test
	void findPathDistanceTest() {
		// when
		PathResponse pathResponse = pathFinder.findPath(StationTest.다대포해수욕장역, StationTest.노포역);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(5);
	}

	@DisplayName("경로를 찾으면 요금을 반환한다")
	@Test
	void findPathFareTest() {
		// when
		PathResponse pathResponse = pathFinder.findPath(StationTest.다대포해수욕장역, StationTest.노포역);

		// then
		assertThat(pathResponse.getFare()).isEqualTo(2350);
	}

	@DisplayName("출발역과 도착역이 같으면 안된다")
	@Test
	void findPath2() {
		// when, then
		assertThatThrownBy(() -> pathFinder.findPath(StationTest.노포역, StationTest.노포역))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("출발역과 도착역이 연결되지 않으면 안된다")
	@Test
	void findPath3() {
		// given
		Section section1 = Section.of(1L, StationTest.노포역, StationTest.서면역, 10);
		Section section2 = Section.of(2L, StationTest.범내골역, StationTest.다대포해수욕장역, 10);

		sections = Sections.of(Arrays.asList(section1, section2));
		PathFinder pathFinder1 = PathFinder.of(sections);

		// when, then
		assertThatThrownBy(() -> pathFinder1.findPath(StationTest.노포역, StationTest.다대포해수욕장역))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("존재하지 않은 출발역 조회할 경우 안된다")
	@Test
	void findPathTest4() {
		// when, then
		assertThatThrownBy(() -> pathFinder.findPath(StationTest.노포역, StationTest.부산진역))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("존재하지 않은 도착역을 조회할 경우 안된다")
	@Test
	void findPathTest5() {
		// when, then
		assertThatThrownBy(() -> pathFinder.findPath(StationTest.부산진역, StationTest.부산진역))
			.isInstanceOf(AppException.class);
	}

	private void 경로_확인(PathResponse pathResponse, List<Station> stations) {
		List<Long> expected = stations.stream()
			.map(Station::getId).collect(Collectors.toList());
		List<Long> results = pathResponse.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());
		assertThat(results).containsAll(expected);
	}

}

package nextstep.subway.path.domain;

import static nextstep.subway.generator.LineGenerator.*;
import static nextstep.subway.generator.SectionGenerator.*;
import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@DisplayName("경로 탐색")
class PathNavigatorTest {

	private Station 교대역;
	private Station 남부터미널역;
	private Station 양재역;

	@BeforeEach
	void setup() {
		교대역 = station(Name.from("교대역"));
		남부터미널역 = station(Name.from("남부터미널역"));
		양재역 = station(Name.from("양재역"));

	}

	/**
	 * 교대역    --- *2호선* (10)---      강남역
	 * |                                |
	 * *3호선*                      *신분당선*
	 * (5)                             (10)
	 * |                                |
	 * 남부터미널역  --- *3호선* (10) ---   양재
	 */

	@DisplayName("경로 탐색 객체 생성")
	@Test
	void findPathTest() {
		assertThatNoException()
			.isThrownBy(() -> PathNavigator.from(Collections.singletonList(신분당선())));
	}

	@DisplayName("최단 경로를 조회할 노선이 없으면 예외 발생")
	@Test
	void findPathFailTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> PathNavigator.from(Collections.emptyList()));
	}

	@DisplayName("최단 경로 조회")
	@Test
	void findShortestPathTest() {
		// given
		PathNavigator navigator = PathNavigator.from(Arrays.asList(신분당선(), 이호선(), 삼호선()));

		// when
		Path path = navigator.path(교대역, 양재역);

		// then
		assertAll(
			() -> assertThat(path.getDistance().value()).isEqualTo(15),
			() -> assertThat(path.getStations()).containsExactly(
				교대역,
				남부터미널역,
				양재역),
			() -> assertThat(path.getSections()).containsExactly(
				section("교대역", "남부터미널역", 5),
				section("남부터미널역", "양재역", 10)
			)
		);
	}

	@DisplayName("출발역과 도착역이 같은 경우 예외")
	@Test
	void findShortestPathWithSameUpDownStationTest() {
		// given
		PathNavigator navigator = PathNavigator.from(Arrays.asList(신분당선(), 이호선(), 삼호선()));

		// when
		assertThatExceptionOfType(InvalidDataException.class)
			.isThrownBy(() -> navigator.path(교대역, 교대역))
			.withMessageContaining("동일할 경우 조회가 불가");
	}

	@DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외")
	@Test
	void findShortestPathWithNotConnectedStationTest() {
		// given
		PathNavigator navigator = PathNavigator.from(Arrays.asList(신분당선(), 이호선(), 삼호선()));
		Station 판교역 = station(Name.from("판교역"));

		// when
		assertThatExceptionOfType(InvalidDataException.class)
			.isThrownBy(() -> navigator.path(교대역, 판교역))
			.withMessageContaining("연결 정보를 찾을 수 없습니다");
	}

	@DisplayName("출발역 혹은 도착역이 존재하지 않는 경우 예외")
	@Test
	void findShortestPathWithNotExistedStationTest() {
		// given
		PathNavigator navigator = PathNavigator.from(Arrays.asList(신분당선(), 이호선(), 삼호선()));

		// when
		assertThatIllegalArgumentException()
			.isThrownBy(() -> navigator.path(null, 양재역));
	}

	private Line 신분당선() {

		return line("신분당선", "red", "강남역", "양재역", 10);
	}

	private Line 이호선() {
		return line("이호선", "red", "교대역", "강남역", 10);
	}

	private Line 삼호선() {
		Line line = line("삼호선", "red", "교대역", "양재역", 15);
		List<Section> sections = line.getSections();
		line.connectSection(section("교대역", "남부터미널역", 5), sections);
		return line;
	}
}
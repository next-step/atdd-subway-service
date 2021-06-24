package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Line 이호선;
	private Line 삼호선;
	private Line 신분당선;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널역 = new Station("남부터미널역");
		신분당선 = new Line("신분당선", "red", 양재역, 강남역, 10);
		이호선 = new Line("2호선", "green", 강남역, 교대역, 10);
		삼호선 = new Line("3호선", "orange", 양재역, 교대역, 5);

		삼호선.addSection(양재역, 남부터미널역, 2);
	}

	@DisplayName("최단 경로를 반환을 테스트")
	@Test
	void testGetShortestPath() {

		List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

		PathFinder pathFinder = new PathFinder(lines);
		Path path = pathFinder.getShortestPath(교대역, 양재역);

		Assertions.assertThat(
			path.getStations())
			.containsExactly(교대역, 남부터미널역, 양재역);
		Assertions.assertThat(path.getDistance()).isEqualTo(5);
	}

	@DisplayName("출발역과 도착역이 같은경우 오류발생")
	@Test
	void testValidateVertex() {
		List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);
		PathFinder pathFinder = new PathFinder(lines);

		Assertions.assertThatThrownBy(() -> {
			pathFinder.getShortestPath(교대역, 교대역);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역과 도착역이 같아 경로를 조회할 수 없음");
	}

	@DisplayName("출발역과 도착역이 연결되어있지 않은 경우 오류발생")
	@Test
	void testUnLinkedStations() {
		Station 수원역 = new Station("수원역");
		Station 안양역 = new Station("안양역");
		Line 일호선 = new Line("일호선", "blue", 수원역, 안양역, 10);

		List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선, 일호선);
		PathFinder pathFinder = new PathFinder(lines);

		Assertions.assertThatThrownBy(() -> {
			pathFinder.getShortestPath(강남역, 수원역);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("구간이 연결되어있지 않아 경로를 찾을 수 없음");
	}
}

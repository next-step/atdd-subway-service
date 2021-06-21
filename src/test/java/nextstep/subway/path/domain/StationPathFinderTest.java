package nextstep.subway.path.domain;

import static java.util.Arrays.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;

class StationPathFinderTest {

	private Line 이호선;
	private Line 삼호선;
	private Line 오호선;
	private Line 신분당선;

	@BeforeEach
	void setup() {
		이호선 = new Line("2호선", "green", 서초역, 교대역, 2);
		이호선.addSection(교대역, 강남역, 3);
		이호선.addSection(강남역, 역삼역, 5);
		신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
		신분당선.addSection(양재역, 양재시민의숲역, 6);
		삼호선 = new Line("삼호선", "orange", 고속터미널역, 교대역, 5);
		삼호선.addSection(교대역, 남부터미널역, 5);
		삼호선.addSection(남부터미널역, 양재역, 5);
		삼호선.addSection(양재역, 도곡역, 5);
		오호선 = new Line("오호선", "orange", 목동역, 오목교역, 5);
	}

	@DisplayName("지하철역 사이의 최단 경로와 거리를 구할 수 있다.")
	@Test
	void pathTest() {
		StationPathFinder graph = StationPathFinder.of(asList(이호선, 신분당선, 삼호선));

		StationPath stationPath = graph.findPath(양재역, 서초역);

		assertThat(stationPath.getStations()).containsExactly(양재역, 강남역, 교대역, 서초역);
		assertThat(stationPath.getTotalDistance()).isEqualTo(9);
	}

	@DisplayName("도착역과 출발역이 같을때는 최단 경로를 구할 수 없다.")
	@Test
	void equalSourceAndTargetTest() {
		StationPathFinder graph = StationPathFinder.of(asList(이호선, 신분당선, 삼호선));

		assertThatThrownBy(() -> graph.findPath(서초역, 서초역))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("도착역과 출발역이 같을 수 없습니다.");
	}

	@DisplayName("출발역과 도착역이 연결이 되어있지 않으면 최단 경로를 구할 수 없다.")
	@Test
	void notConnectSourceAndTargetTest() {
		StationPathFinder graph = StationPathFinder.of(asList(이호선, 신분당선, 삼호선, 오호선));

		assertThatThrownBy(() -> graph.findPath(서초역, 목동역))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
	}

	@DisplayName("출발역과 도착역이 둘 다 존재해야 최단 경로를 구할 수 있다.")
	@Test
	void notExistConnectSourceAndTargetTest() {
		StationPathFinder graph = StationPathFinder.of(asList(이호선, 신분당선));

		assertThatThrownBy(() -> graph.findPath(고속터미널역, 강남역), "출발역이 없는 경우")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역 또는 도착역이 존재하지 않습니다.");

		assertThatThrownBy(() -> graph.findPath(강남역, 고속터미널역), "도착역이 없는 경우")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역 또는 도착역이 존재하지 않습니다.");

		assertThatThrownBy(() -> graph.findPath(남부터미널역, 고속터미널역), "출발역 도착역 없는 경우")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역 또는 도착역이 존재하지 않습니다.");
	}
}

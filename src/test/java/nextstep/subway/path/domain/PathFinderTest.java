package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.NotConnectedStationException;
import nextstep.subway.path.exception.SameStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

class PathFinderTest {

	public static final long 존재하지_않는_역_ID = 999L;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Station 부평구청역;
	private Station 장암역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Line 칠호선;

	@BeforeEach
	void setup() {
		강남역 = new Station(1L, "강남역");
		양재역 = new Station(2L, "양재역");
		교대역 = new Station(3L, "교대역");
		남부터미널역 = new Station(4L, "남부터미널역");
		부평구청역 = new Station(5L, "부평구청역");
		장암역 = new Station(6L, "장암역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10, 500);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 0);
		칠호선 = new Line("칠호선", "bg-red-600", 부평구청역, 장암역, 100, 0);
		삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
	}

	@DisplayName("getShortestPath메서드는 최단거리 역목록과 거리 정보를 포함하는 PathResponse를 반환한다.")
	@Test
	void getShortestPath() {
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 남부터미널역.getId(), 30);
		PathResponse pathResponse = pathFinder.getShortestPathResponse();
		List<String> stationNames = pathResponse.getStations()
			.stream()
			.map(StationResponse::getName)
			.collect(Collectors.toList());

		assertAll(
			() -> assertThat(stationNames).containsExactlyElementsOf(
				Arrays.asList(강남역.getName(), 양재역.getName(), 남부터미널역.getName())),
			() -> assertThat(pathResponse.getFare()).isEqualTo(2250),
			() -> assertThat(pathResponse.getDistance()).isEqualTo(12)
		);
	}

	@DisplayName("getShortestPath메서드는 동일한 출발역, 도착역을 지정해서 조회하면 SameStationException이 발생한다.")
	@Test
	void sameStation() {
		assertThatExceptionOfType(SameStationException.class)
			.isThrownBy(() -> {
				PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 강남역.getId(), 30);
				pathFinder.getShortestPathResponse();
			});
	}

	@DisplayName("getShortestPath메서드는 노선이 연결되어 있지 않은 역을 지정해서 조회 NotConnectedStationException이 발생한다.")
	@Test
	void notConnectedStation() {
		assertThatExceptionOfType(NotConnectedStationException.class)
			.isThrownBy(() -> {
				PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 부평구청역.getId(), 30);
				pathFinder.getShortestPathResponse();
			});
	}

	@DisplayName("getShortestPath메서드는 존재하지 않는 출발역을 조회하면 IllegalArgumentException이 발생한다.")
	@Test
	void wrongStation() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 존재하지_않는_역_ID, 부평구청역.getId(), 30);
				pathFinder.getShortestPathResponse();
			});
	}

}
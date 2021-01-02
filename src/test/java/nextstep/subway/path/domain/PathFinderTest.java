package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineResponse;
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

	@DisplayName("getShortestPathStationsByDistance메서드는 최단거리에 포함되는 역 목록을 반환한다.")
	@Test
	void getShortestPathStationsByDistance() {
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 남부터미널역.getId());

		List<StationResponse> shortestStations = pathFinder.getShortestPathStationsByDistance();
		List<String> stationNames = shortestStations.stream()
			.map(StationResponse::getName)
			.collect(Collectors.toList());

		assertThat(stationNames).containsExactlyElementsOf(
			Arrays.asList(강남역.getName(), 양재역.getName(), 남부터미널역.getName()));
	}

	@DisplayName("getShortestDistance메서드는 최단거리를 반환한다.")
	@Test
	void getShortestDistance() {
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 남부터미널역.getId());

		int result = pathFinder.getShortestDistance();

		assertThat(result).isEqualTo(12);
	}

	@DisplayName("getShortestPathLinesByDistance메서드는 최단 거리로 지나는 노선 목록을 반환한다.")
	@Test
	void getShortestPathLinesByDistance() {
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 남부터미널역.getId());

		List<LineResponse> lines = pathFinder.getShortestPathLinesByDistance();
		List<String> lineNames = lines.stream()
			.map(LineResponse::getName)
			.collect(Collectors.toList());

		assertThat(lineNames).contains("신분당선", "삼호선");

	}

	@DisplayName("PathFinder는 객체를 생성할 때 동일한 출발역, 도착역 전달하면 SameStationException이 발생한다.")
	@Test
	void sameStation() {
		assertThatExceptionOfType(SameStationException.class)
			.isThrownBy(() -> {
				new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 강남역.getId());
			});
	}

	@DisplayName("PathFinder는 객체를 생성할 때 노선이 연결되어 있지 않은 역을 전달하면 NotConnectedStationException이 발생한다.")
	@Test
	void notConnectedStation() {
		assertThatExceptionOfType(NotConnectedStationException.class)
			.isThrownBy(() -> {
				new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 강남역.getId(), 부평구청역.getId());
			});
	}

	@DisplayName("PathFinder는 객체를 생성할 때 존재하지 않는 출발역을 전달하면 IllegalArgumentException이 발생한다.")
	@Test
	void wrongStation() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 칠호선), 존재하지_않는_역_ID, 부평구청역.getId());
			});
	}

}
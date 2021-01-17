package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayMapTest {
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Station 잠실역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private SubwayMap 전체_지하철_노선;

	/**
	 * 교대역    ---   *2호선* (10)  ---   강남역   ----  *2호선* (5) ---- 잠실역
	 * |                                  |
	 * *3호선* (3)                     *신분당선* (10)
	 * |                                  |
	 * 남부터미널역  ---  *3호선* (2)   ---   양재
	 */

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널역 = new Station("남부터미널역");
		잠실역 = new Station("잠실역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);
		이호선.addSection(강남역, 잠실역, 5);

		전체_지하철_노선 = new SubwayMap(Arrays.asList(신분당선, 이호선, 삼호선));
	}

	@Test
	@DisplayName("전체 노선의 전체 구간을 조회하면 노선에 등록된 모든 구간이 조회되어야한다.")
	void findAllSections() {
		//given
		Section section1 = new Section(신분당선, 강남역, 양재역, 10);
		Section section2 = new Section(이호선, 교대역, 강남역, 10);
		Section section3 = new Section(이호선, 강남역, 잠실역, 5);
		Section section4 = new Section(삼호선, 교대역, 남부터미널역, 3);
		Section section5 = new Section(삼호선, 남부터미널역, 양재역, 2);

		//when
		List<Section> allSections = 전체_지하철_노선.allSections();

		//then
		assertThat(allSections).containsExactlyInAnyOrder(section1, section2, section3, section4, section5);
	}

	@Test
	@DisplayName("최단거리를 구하면 최단거리 구간의 역 목록과 최단거리가 계산되어야 한다.")
	void registerGraph() {
		//when
		ShortestPath path = 전체_지하철_노선.findShortestPath(잠실역, 남부터미널역);

		//then
		assertThat(path.getStations()).containsExactly(잠실역, 강남역, 양재역, 남부터미널역);
		assertThat(path.getDistance()).isEqualTo(17);
	}

	@Test
	@DisplayName("출발역과 도착역이 연결되지 않은 경우 IllegalArgumentException을 throw 해야한다.")
	void notConnectedSourceAndTarget() {
		//given
		Station 연결안된_역1 = new Station(100L, "연결안된_역1");
		Station 연결안된_역2 = new Station(200L, "연결안된_역2");
		Line 신규노선 = new Line("연결안된노선", "rainbow", 연결안된_역1, 연결안된_역2, 10);
		전체_지하철_노선 = new SubwayMap(Arrays.asList(신분당선, 이호선, 삼호선, 신규노선));

		//when/then
		assertThatThrownBy(() -> 전체_지하철_노선.findShortestPath(교대역, 연결안된_역1))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@CsvSource(value = {"1:1250", "9:1250", "10:1250"
		, "11:1350", "15:1350", "16:1450", "20:1450", "26:1650", "50:2050"
		, "51:2150", "58:2150", "59:2250", "66:2250", "67:2350", "75:2450"
	}, delimiter = ':')
	@DisplayName("최단거리에 해당하는 요금이 계산되어야한다.")
	void calculateFare(Long distance, int fare) {
		//given
		ShortestPath shortestPath = new ShortestPath(new ArrayList<>(), distance);

		//when-then
		assertThat(shortestPath.getFare()).isEqualTo(fare);
	}
}

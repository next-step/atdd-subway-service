package nextstep.subway.path.domain;

import nextstep.subway.common.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 교대역;
	private Station 건대입구역;
	private Station 시청역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private LineMap lineMap;
	private Path 교대_양재;
	private Path 남부터미널_건대입구;
	private Path 양재_시청;

	/**
	 * [지하철 노선도]
	 *
	 *             거리 4                  거리 20                 거리 34
	 * 교대역    --- *2호선* ---   강남역  --- *2호선* --- 건대입구역 --- *2호선 --- 시청역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * 거리 3                     거리 10
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재
	 *                거리 4
	 */

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		남부터미널역 = new Station("남부터미널역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		건대입구역 = new Station("건대입구역");
		시청역 = new Station("시청역");

		신분당선 = new Line("신분당선", "노랑", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "초록", 교대역, 강남역, 4);
		이호선.addLineStation(강남역, 건대입구역, 20);
		이호선.addLineStation(건대입구역, 시청역, 34);
		삼호선 = new Line("삼호선", "주황", 교대역, 남부터미널역, 3);
		삼호선.addLineStation(남부터미널역, 양재역, 4);
		lineMap = new LineMap(Arrays.asList(신분당선, 이호선, 삼호선));
		교대_양재 = lineMap.calculate(교대역, 양재역);
		남부터미널_건대입구 = lineMap.calculate(남부터미널역, 건대입구역);
		양재_시청 = lineMap.calculate(양재역, 시청역);
	}

	@DisplayName("연령에 따라 할인이 적용되어 지하철 이용 금액이 다르다.")
	@ParameterizedTest
	@CsvSource(value = {"6,800", "13,1070", "19,1250"})
	void getFare(int age, int fare) {
		assertThat(교대_양재.getFare(age)).isEqualTo(new Fare(fare));
	}

	@DisplayName("연령별 할인된 금액(지하철거리 10KM 초과시)")
	@ParameterizedTest
	@CsvSource(value = {"6,1050", "13,1470", "19,1750"})
	void getFare_10KM초과(int age, int fare) {
		assertThat(남부터미널_건대입구.getFare(age)).isEqualTo(new Fare(fare));
	}

	@DisplayName("연령별 할인된 금액(지하철거리 50KM 초과시)")
	@ParameterizedTest
	@CsvSource(value = {"6,1200", "13,1710", "19,2050"})
	void getFare_50KM초과(int age, int fare) {
		assertThat(양재_시청.getFare(age)).isEqualTo(new Fare(fare));
	}

	@DisplayName("최단 경로로 이동시 거쳐가는 역을 구한다.")
	@Test
	void getStations() {
		assertThat(교대_양재.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
		assertThat(남부터미널_건대입구.getStations()).containsExactly(남부터미널역, 교대역, 강남역, 건대입구역);
		assertThat(양재_시청.getStations()).containsExactly(양재역, 강남역, 건대입구역, 시청역);
	}

	@DisplayName("최단 경로로 이동시 거리를 구한다.")
	@Test
	void getDistance() {
		assertThat(교대_양재.getDistance()).isEqualTo(new Distance(7));
		assertThat(남부터미널_건대입구.getDistance()).isEqualTo(new Distance(27));
		assertThat(양재_시청.getDistance()).isEqualTo(new Distance(64));
	}
}

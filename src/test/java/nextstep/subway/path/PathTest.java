package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathCalculateException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

	/**
	 *              거리 5
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * 거리 3                     거리 10
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재
	 *              거리 2
	 */

	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 교대역;
	private Station 노원역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Path path;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		남부터미널역 = new Station("남부터미널역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		노원역 = new Station("노원역");

		신분당선 = new Line("신분당선", "노랑", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "초록", 교대역, 강남역, 5);
		삼호선 = new Line("삼호선", "주황", 교대역, 남부터미널역, 3);
		삼호선.addLineStation(남부터미널역, 양재역, 2);
		path = new Path(Arrays.asList(신분당선, 이호선, 삼호선));
	}

	@Test
	void calculate1() {
		// when
		List<Station> stations = path.calculate(강남역, 양재역);

		// then
		assertThat(stations)
				.map(Station::getName)
				.containsExactly("강남역", "양재역");
	}

	@Test
	void calculate2() {
		// when
		List<Station> stations = path.calculate(강남역, 남부터미널역);

		// then
		assertThat(stations)
				.map(Station::getName)
				.containsExactly("강남역", "교대역", "남부터미널역");
	}

	@DisplayName("노선에 포함되어 있지 않은 역을 계산할경우")
	@Test
	void calculate_NotIncluded() {
		// when
		assertThatThrownBy(() -> path.calculate(강남역, 노원역))
				.isInstanceOf(PathCalculateException.class)
				.hasMessageContaining("경로에 포함되어 있지 않은 역");
	}

	@DisplayName("출발지와 도착지가 서로 동일할 경우")
	@Test
	void calculate_SameStation() {
		// when
		assertThatThrownBy(() -> path.calculate(강남역, 강남역))
				.isInstanceOf(PathCalculateException.class)
				.hasMessageContaining("출발지와 도착지가 같습니다.");
	}
}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathCalculateException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class LineMapTest {

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
	private Station 양재역;
	private Station 노원역;
	private Line 이호선;
	private LineMap lineMap;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		노원역 = new Station("노원역");

		이호선 = new Line("이호선", "초록", 양재역, 강남역, 5, 0);
		lineMap = new LineMap(Arrays.asList(이호선));
	}

	@DisplayName("최적의 경로를 계산한 결과의 객체를 만든다.")
	@Test
	void calculate() {
		// when
		Path path = lineMap.calculate(강남역, 양재역);

		// then
		assertThat(path.getStations())
				.map(Station::getName)
				.containsExactly("강남역", "양재역");
	}

	@DisplayName("노선에 포함되어 있지 않은 역을 계산할경우")
	@Test
	void calculate_NotIncluded() {
		// when
		assertThatThrownBy(() -> lineMap.calculate(강남역, 노원역))
				.isInstanceOf(PathCalculateException.class)
				.hasMessageContaining("경로에 포함되어 있지 않은 역");
	}

	@DisplayName("출발지와 도착지가 서로 동일할 경우")
	@Test
	void calculate_SameStation() {
		// when
		assertThatThrownBy(() -> lineMap.calculate(강남역, 강남역))
				.isInstanceOf(PathCalculateException.class)
				.hasMessageContaining("출발지와 도착지가 같습니다.");
	}
}

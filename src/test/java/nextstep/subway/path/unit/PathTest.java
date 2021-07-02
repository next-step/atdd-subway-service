package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

@DisplayName("Path 단위테스트")
public class PathTest {

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널역 = new Station("남부터미널역");

		신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "blue", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "black", 교대역, 양재역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);
	}

	@DisplayName("짧은 경로에 속한 역 목록")
	@Test
	void getShortestStations() {
		Path path = new Path(Arrays.asList(신분당선, 이호선, 삼호선));

		assertThat(path.getShortestStations(교대역, 양재역)).extracting("name").containsExactly("교대역", "남부터미널역", "양재역");
	}

	@DisplayName("짧은 경로의 길이")
	@Test
	void getShortestDistance() {
		Path path = new Path(Arrays.asList(신분당선, 이호선, 삼호선));

		assertThat(path.getShortestDistance(교대역, 양재역)).isEqualTo(5);
	}
}

package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.route.PathRoute;
import nextstep.subway.station.domain.Station;

class PathFinderTest extends AcceptanceTest {

	private Line 신분당선;
	private Line 삼호선;
	private Line 이호선;

	private Station 교대역;
	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;

	@Autowired
	private PathFinder pathFinder;

	@BeforeEach
	void setup() {
		super.setUp();

		//given
		신분당선 = new Line("신분당선", "red");
		삼호선 = new Line("삼호선", "orange");
		이호선 = new Line("이호선", "green");

		교대역 = new Station("교대역");
		강남역 = new Station("강남역");
		남부터미널역 = new Station("남부터미널역");
		양재역 = new Station("양재역");
	}

	@DisplayName("두 지점 간의 최단 경로 구하기")
	@Test
	void findShortestPath() {
		//given
		List<Sections> lineSections = new ArrayList<>();
		신분당선.getSections().addSection(신분당선, 강남역, 양재역, 10);
		이호선.getSections().addSection(이호선, 교대역, 강남역, 10);
		삼호선.getSections().addSection(삼호선, 교대역, 남부터미널역, 3);
		삼호선.getSections().addSection(삼호선, 남부터미널역, 양재역, 2);

		// when
		PathRoute bestPath = pathFinder.findShortestPath(Arrays.asList(신분당선,이호선,삼호선), 교대역, 양재역);

		//then
		assertThat(bestPath.getDistance()).isEqualTo(5);
		assertThat(bestPath.getStationsRoute()).containsAnyElementsOf(Arrays.asList(교대역,남부터미널역,양재역));
	}
}
package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
	private PathFinder pathFinder;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널 = new Station("남부터미널");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
		삼호선.addSection(new Section(삼호선, 교대역, 남부터미널, new Distance(3)));

		pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
	}

	@Test
	void findShortestPath() {
		Path path = pathFinder.findShortestPath(강남역, 양재역);

		List<Station> stations = path.getStations();
		assertThat(stations).hasSize(2);
		assertThat(stations.get(0).getName()).isEqualTo(강남역.getName());
		assertThat(stations.get(1).getName()).isEqualTo(양재역.getName());
		assertThat(path.getDistance().value()).isEqualTo(10);
	}
}

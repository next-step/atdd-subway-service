package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.SubwayPath;
import nextstep.subway.station.domain.Station;

@DisplayName("경로 찾기 테스트")
public class PathTest {

	@Test
	void findPath() {
		// given
		long id = 1L;
		Station 강남역 = new Station(id++, "강남역");
		Station 양재역 = new Station(id++, "양재역");
		Station 교대역 = new Station(id++, "교대역");
		Station 남부터미널역 = new Station(id, "남부터미널역");

		Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);

		int expectedDistance = 20;

		// when
		PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선));
		SubwayPath subwayPath = pathFinder.getSubwayPath(양재역, 교대역);

		// then
		assertThat(subwayPath.getStations()).containsExactly(양재역, 강남역, 교대역);
		assertThat(subwayPath.getDistance()).isEqualTo(expectedDistance);

	}

}

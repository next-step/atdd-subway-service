package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.SubwayPath;
import nextstep.subway.station.domain.Station;

@DisplayName("경로 찾기 테스트")
public class PathTest {

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	private List<Line> lines;
	private PathFinder pathFinder;
	private SubwayPath subwayPath;

	@BeforeEach
	public void setup() {
		long id = 1L;
		강남역 = new Station(id++, "강남역");
		양재역 = new Station(id++, "양재역");
		교대역 = new Station(id++, "교대역");
		남부터미널역 = new Station(id, "남부터미널역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);

		lines = Arrays.asList(신분당선, 이호선, 삼호선);
		pathFinder = new PathFinder(lines);

	}

	@DisplayName("경로 조회")
	@Test
	void findPath() {
		// given
		int expectedDistance = 5;
		subwayPath = pathFinder.getSubwayPath(양재역, 교대역);

		// then
		assertThat(subwayPath.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
		assertThat(subwayPath.getDistance()).isEqualTo(expectedDistance);

	}

	@DisplayName("경로 조회 예외 - 출발역과 도착역이 같은 경우")
	@Test
	void findPathThrowExceptionWhenSameSourceAndTarget() {
		assertThatIllegalArgumentException().isThrownBy(
			() -> pathFinder.getSubwayPath(양재역, 양재역)
		);
	}

}

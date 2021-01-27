package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

	private long id = 1L;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	private List<Line> lines;
	private SubwayPath subwayPath;

	@BeforeEach
	public void setup() {
		강남역 = new Station(id++, "강남역");
		양재역 = new Station(id++, "양재역");
		교대역 = new Station(id++, "교대역");
		남부터미널역 = new Station(id++, "남부터미널역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);

		lines = Arrays.asList(신분당선, 이호선, 삼호선);


	}

	@DisplayName("경로 조회")
	@Test
	void findPath() {
		// given
		int expectedDistance = 5;
		PathFinder pathFinder = new PathFinder(lines, 양재역, 교대역);
		subwayPath = pathFinder.findPath();

		// then
		assertThat(subwayPath.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
		assertThat(subwayPath.getDistance()).isEqualTo(expectedDistance);

	}

	@DisplayName("경로 조회 예외 - 출발역과 도착역이 같은 경우")
	@Test
	void findPathThrowExceptionWhenSameSourceAndTarget() {

		assertThatIllegalArgumentException().isThrownBy(
			() -> new PathFinder(lines, 양재역, 양재역)
		);
	}

	@DisplayName("경로 조회 예외 - 출발역과 도착역이 연결되어 있지 않은 경우")
	@Test
	void findPathThrowExceptionWhen() {
		assertThatIllegalArgumentException().isThrownBy(
			() -> new PathFinder(Arrays.asList(신분당선, 이호선), 강남역, 남부터미널역)
		);
	}

	@DisplayName("경로 조회 예외 - 존재하지 않은 출발역이나 도착역을 조회할 경우")
	@Test
	void findPathThrowExceptionWhenNotExistsStation() {
		Station 용산역 = new Station(id++, "용산역");

		assertAll(
			() -> assertThatIllegalArgumentException().isThrownBy(
				() -> new PathFinder(lines, 강남역, 용산역)
			),
			() -> assertThatIllegalArgumentException().isThrownBy(
				() -> new PathFinder(lines, 용산역, 강남역)
			)
		);

	}

}

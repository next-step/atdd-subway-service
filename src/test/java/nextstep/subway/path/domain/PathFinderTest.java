package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

	private List<Line> lines;
	private Station 교대역;
	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 석촌역;
	private Station 송파역;
	private Station 잠실역;

	@BeforeEach
	void setUp() {
		교대역 = new Station("교대역");
		강남역 = new Station("강남역");
		남부터미널역 = new Station("남부터미널역");
		양재역 = new Station( "양재역");
		석촌역 = new Station("석촌역");
		송파역 = new Station("송파역");
		잠실역 = new Station("잠실역");

		Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
		Line 팔호선 = new Line("팔호선", "bg-red-600", 석촌역, 송파역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);

		lines = new ArrayList<>();
		lines.add(신분당선);
		lines.add(이호선);
		lines.add(삼호선);
		lines.add(팔호선);
	}

	@DisplayName("findPath 테스트 : 일반적인 경우")
	@Test
	public void findPath_happyPath() {
		Optional<Path> path = PathFinder.findPath(lines, 교대역, 양재역);

		assertThat(path).isPresent();
		assertThat(path).map(Path::getDistance).hasValue(5L);
		assertThat(path.get().getStations()).containsExactly(교대역, 남부터미널역, 양재역);
	}

	@DisplayName("findPath 테스트 : 역이 특정 노선에는 포함되어 있으나, 도달할 수 없는 경우")
	@Test
	public void findPath_exceptionCase1() {
		Optional<Path> path = PathFinder.findPath(lines, 교대역, 석촌역);

		assertThat(path).isNotPresent();
	}

	@DisplayName("findPath 테스트 : 역이 아무 노선에도 포함되어있지 않은 경우")
	@Test
	public void findPath_exceptionCase2() {
		Optional<Path> path = PathFinder.findPath(lines, 교대역, 잠실역);

		assertThat(path).isNotPresent();
	}

	@DisplayName("findPath 테스트 : 출발역과 도착역이 같은 경우")
	@Test
	public void findPath_exceptionCase3() {
		assertThatThrownBy(() -> PathFinder.findPath(lines, 교대역, 교대역))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

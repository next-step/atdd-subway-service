package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.shortest.ShortestPath;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;

public class PathTest {

	@DisplayName("노선 목록이 없을 시 예외발생")
	@Test
	void path_not_found_lines() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Path.of(null));
	}

	/**
	 * 교대역 ------- *2호선(10)* ------- 강남역
	 * |                        		|
	 * *3호선(3)*              		*신분당선(10)*
	 * |                        		|
	 * 남부터미널역 ---- *3호선(2)* ---- 양재역
	 */
	@DisplayName("지하철 경로 조회")
	@Test
	void findShortest() {
		final Station 강남역 = new Station("강남역");
		final Station 양재역 = new Station("양재역");
		final Station 교대역 = new Station("교대역");
		final Station 남부터미널역 = new Station("남부터미널역");
		final Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10, 400);
		final Line 이호선 = new Line("이호선", "green", 강남역, 교대역, 10, 0);
		final Line 삼호선 = new Line("삼호선", "orange", 양재역, 교대역, 5, 300);
		삼호선.addSection(양재역, 남부터미널역, 2);

		final List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);
		final Path path = Path.of(lines);
		final ShortestPath shortestPath = path.findShortest(강남역, 남부터미널역);

		assertThat(shortestPath.getStations()).containsExactly(강남역, 양재역, 남부터미널역);
		assertThat(shortestPath.getDistance()).isEqualTo(12d);
	}

	@DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외발생")
	@Test
	void findShortest_not_found() {
		final Station 여의도역 = new Station("여의도역");
		final Station 신길역 = new Station("신길역");
		final Station 잠실역 = new Station("잠실역");
		final Station 석촌역 = new Station("석촌역");
		final List<Line> lines = Arrays.asList(
			new Line("5호선", "violet", 여의도역, 신길역, 3, 100),
			new Line("8호선", "pink", 잠실역, 석촌역, 4, 200)
		);
		final Path path = Path.of(lines);

		assertThatExceptionOfType(PathNotFoundException.class)
			.isThrownBy(() -> path.findShortest(여의도역, 잠실역));
	}

	@DisplayName("노선에 존재하지 않는 역으로 경로 생성시 예외발생")
	@Test
	void findShortest_not_found_station() {
		final Station 서울역 = new Station("서울역");
		final Station 시청역 = new Station("시청역");
		final List<Line> lines = Arrays.asList(
			new Line("1호선", "navy", 서울역, 시청역, 3, 0)
		);
		final Path path = Path.of(lines);

		final Station 강남역 = new Station("강남역");
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> path.findShortest(강남역, 시청역));
	}

	@DisplayName("출발역과 도착역이 동일한 경로 생성시 예외발생")
	@Test
	void findShortest_same_station_ids() {
		final Station 미금역 = new Station("미금역");
		final Station 정자역 = new Station("정자역");
		final List<Line> lines = Arrays.asList(
			new Line("분당선", "yellow", 미금역, 정자역, 1, 400)
		);
		final Path path = Path.of(lines);

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> path.findShortest(미금역, 미금역));
	}
}

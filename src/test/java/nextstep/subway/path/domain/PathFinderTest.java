package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.InvalidPathException;
import nextstep.subway.common.exception.SameStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

class PathFinderTest {

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* ---   양재
	 */

	PathFinder pathFinder;
	Station 강남역;
	Station 남부터미널역;
	Station 교대역;
	Station 양재역;
	Station 주안역;
	Station 부천역;
	List<Line> lines;

	@BeforeEach
	void setup() {
		강남역 = new Station("강남역");
		교대역 = new Station("교대역");
		양재역 = new Station("양재역");
		남부터미널역 = new Station("남부터미널");
		주안역 = new Station("주안역");
		부천역 = new Station("부천역");

		Line 일호선 = new Line("일호선", "bg-red-600", 주안역, 부천역, 20);
		Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
		삼호선.addStation(교대역, 남부터미널역, 3);

		lines = Arrays.asList(일호선, 이호선, 신분당선, 삼호선);
		pathFinder = new PathFinder(강남역, 남부터미널역, lines);
	}

	@DisplayName("최단 경로를 반환한다")
	@Test
	void 최단_경로를_반환한다() {
		List<Station> pathResponse = pathFinder.findShortestPath();
		assertThat(pathResponse).contains(강남역, 양재역, 남부터미널역);
	}

	@DisplayName("최단 거리를 반환한다")
	@Test
	void 최단_거리를_반환한다() {
		int distance = pathFinder.shortestPathDistance();

		assertThat(distance).isEqualTo(12);
	}

	@DisplayName("출,도착지가 동일한 경우, 에러가 발생한다.")
	@Test
	void 출_도착지_같은_경우_에러() {
		assertThrows(SameStationException.class, () -> new PathFinder(강남역, 강남역, lines));
	}

	@DisplayName("출발지와 도착지가 연결되지 않는 경우 에러가 발생한다.")
	@Test
	void 출_도착지가_연결되지_않는_경우_에러() {
		assertThrows(InvalidPathException.class, () -> new PathFinder(주안역, 강남역, lines).findShortestPath());
	}

	@DisplayName("출발지와 도착지가 연결되지 않는 경우 에러가 발생한다.")
	@Test
	void 출_도착지가_존재하지_않는_경우_에러() {
		Station 부평역 = new Station("부평역");
		assertThrows(IllegalArgumentException.class, () -> new PathFinder(부평역, 강남역, lines));
	}
}

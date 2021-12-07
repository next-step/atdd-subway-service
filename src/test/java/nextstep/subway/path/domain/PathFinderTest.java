package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.PathAcceptanceTest;
import nextstep.subway.station.domain.Station;

/**
 * @see PathAcceptanceTest
 */
@DisplayName("경로 파인더")
public class PathFinderTest {

	@DisplayName("경로 파인더 생성")
	@Test
	void of() {
		// given
		List<Line> lines = Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선());

		// when
		PathFinder pathFinder = PathFinder.of(lines);

		// then
		assertThat(pathFinder).isNotNull();
	}

	@DisplayName("경로 찾기")
	@ParameterizedTest
	@MethodSource("findMethodSource")
	void find(Station source, Station target, List<Station> expectedStations, int expectedDistance) {
		// given
		PathFinder pathFinder = PathFinder.of(Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선()));

		// when
		Path path = pathFinder.find(source, target);

		// then
		assertAll(
			() -> assertThat(path).isNotNull(),
			() -> assertThat(path.getStations()).isEqualTo(expectedStations),
			() -> assertThat(path.getDistance()).isEqualTo(expectedDistance));
	}

	private static List<Arguments> findMethodSource() {
		return Arrays.asList(
			Arguments.of(
				양재시민의숲역(),
				선릉역(),
				Arrays.asList(
					양재시민의숲역(),
					양재역(),
					강남역(),
					역삼역(),
					선릉역()),
				Stream.of(
						양재역_양재시민의숲역_구간(),
						강남역_양재역_구간(),
						강남역_역삼역_구간(),
						역삼역_선릉역_구간())
					.map(Section::getDistance)
					.reduce(Integer::sum)
					.get()
			),
			Arguments.of(
				역삼역(),
				남부터미널역(),
				Arrays.asList(
					역삼역(),
					강남역(),
					양재역(),
					남부터미널역()),
				Stream.of(
						강남역_역삼역_구간(),
						강남역_양재역_구간(),
						남부터미널역_양재역_구간())
					.map(Section::getDistance)
					.reduce(Integer::sum)
					.get()
			),
			Arguments.of(
				교대역(),
				역삼역(),
				Arrays.asList(
					교대역(),
					강남역(),
					역삼역()),
				Stream.of(
						교대역_강남역_구간(),
						강남역_역삼역_구간())
					.map(Section::getDistance)
					.reduce(Integer::sum)
					.get()
			)
		);
	}

	@DisplayName("경로 찾기 실패 - 출발역과 도착역이 같은 경우")
	@Test
	void findFailOnEqual() {
		// given
		PathFinder pathFinder = PathFinder.of(Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선()));

		// when & then
		assertThatThrownBy(() -> pathFinder.find(강남역(), 강남역()))
			.isInstanceOf(CanNotFindPathException.class);
	}

	@DisplayName("경로 찾기 실패 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
	@Test
	void findFailOnNotConnected() {
		// given
		PathFinder pathFinder = PathFinder.of(Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선()));

		// when & then
		assertThatThrownBy(() -> pathFinder.find(양재시민의숲역(), 중앙역()))
			.isInstanceOf(CanNotFindPathException.class);
	}

	@DisplayName("경로 찾기 실패 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
	@Test
	void findFailOnNotExist() {
		// given
		PathFinder pathFinder = PathFinder.of(Arrays.asList(
			신분당선(),
			이호선(),
			삼호선(),
			사호선()));

		// when & then
		assertThatThrownBy(() -> pathFinder.find(강남역(), 존재하지않는역()))
			.isInstanceOf(CanNotFindPathException.class);
	}
}

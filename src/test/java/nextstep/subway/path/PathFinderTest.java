package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.common.exception.BadParameterException;
import nextstep.subway.line.LineTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.FindShortestPathResult;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathFinderTest {
	private Line 신분당선;
	private Line 구분당선;

	@BeforeEach
	void setUp() {
		신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
	}

	@Test
	@DisplayName("최단 경로를 조회한다.")
	void findShortestPath() {
		Section 삼성_선릉_구간 = new Section(신분당선, StationTest.삼성역, StationTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, StationTest.선릉역, StationTest.역삼역, 7);
		Section 역삼_강남_구간 = new Section(신분당선, StationTest.역삼역, StationTest.강남역, 5);
		Section 삼성_강남_구간 = new Section(구분당선, StationTest.삼성역, StationTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);
		신분당선.addSection(역삼_강남_구간);
		구분당선.addSection(삼성_강남_구간);

		PathFinder pathFinder = new PathFinder();
		FindShortestPathResult path = pathFinder.findShortestPath(
			StationTest.삼성역,
			StationTest.역삼역,
			new Sections(Stream.of(신분당선.getSections(), 구분당선.getSections())
				.flatMap(Collection::stream)
				.collect(Collectors.toList()
				))
		);

		assertThat(path.getStations()).isEqualTo(
			Arrays.asList(
				StationResponse.of(StationTest.삼성역), StationResponse.of(StationTest.강남역),
				StationResponse.of(StationTest.역삼역)
			));
		assertThat(path.getDistance()).isEqualTo(9);
	}

	@Test
	@DisplayName("최단 경로를 조회한다2")
	void findShortestPath2() {
		Station 가로지르는역1 = new Station("가로지르는역1");
		Station 가로지르는역2 = new Station("가로지르는역2");
		Station 가로지르는역3 = new Station("가로지르는역3");
		Section 삼성_선릉_구간 = new Section(신분당선, StationTest.삼성역, StationTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, StationTest.선릉역, StationTest.역삼역, 7);
		Section 역삼_강남_구간 = new Section(신분당선, StationTest.역삼역, StationTest.강남역, 5);
		Section 삼성_강남_구간 = new Section(구분당선, StationTest.삼성역, StationTest.강남역, 4);
		Section 가로지르는역1_삼성_구간 = new Section(구분당선, 가로지르는역1, StationTest.삼성역, 1);
		Section 가로지르는역1_가로지르는역2_구간 = new Section(구분당선, 가로지르는역2, 가로지르는역1, 1);
		Section 가로지르는역2_가로지르는역3_구간 = new Section(구분당선, 가로지르는역3, 가로지르는역2, 1);
		Section 역삼_가로지르는역3_구간 = new Section(구분당선, StationTest.역삼역, 가로지르는역3, 1);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);
		신분당선.addSection(역삼_강남_구간);
		구분당선.addSection(가로지르는역1_삼성_구간);
		구분당선.addSection(가로지르는역1_가로지르는역2_구간);
		구분당선.addSection(가로지르는역2_가로지르는역3_구간);
		구분당선.addSection(역삼_가로지르는역3_구간);
		구분당선.addSection(삼성_강남_구간);

		PathFinder pathFinder = new PathFinder();
		FindShortestPathResult path = pathFinder.findShortestPath(
			StationTest.삼성역,
			StationTest.역삼역,
			new Sections(Stream.of(신분당선.getSections(), 구분당선.getSections())
				.flatMap(Collection::stream)
				.collect(Collectors.toList()
				))
		);

		assertThat(path.getStations()).isEqualTo(
			Arrays.asList(
				StationResponse.of(StationTest.삼성역), StationResponse.of(가로지르는역1),
				StationResponse.of(가로지르는역2), StationResponse.of(가로지르는역3),
				StationResponse.of(StationTest.역삼역)
			));
		assertThat(path.getDistance()).isEqualTo(4);
	}

	@Test
	@DisplayName("최단 경로 조회시 출발역과 도착역이 같으면 예외")
	void findShortestPath_sourceAndTargetEqual_Exception() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, StationTest.삼성역, StationTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, StationTest.선릉역, StationTest.역삼역, 7);
		Section 역삼_강남_구간 = new Section(구분당선, StationTest.역삼역, StationTest.강남역, 5);
		Section 삼성_강남_구간 = new Section(구분당선, StationTest.삼성역, StationTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);
		구분당선.addSection(역삼_강남_구간);
		구분당선.addSection(삼성_강남_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(
			StationTest.삼성역,
			StationTest.삼성역,
			new Sections(
				Stream.of(신분당선.getSections(), 구분당선.getSections())
					.flatMap(Collection::stream)
					.collect(Collectors.toList()
					))))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지와 도착지가 같아 경로를 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외")
	void findShortestPath_notConnectedSourceAndDest_Exception() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, StationTest.삼성역, StationTest.선릉역, 5);
		Section 역삼_강남_구간 = new Section(구분당선, StationTest.역삼역, StationTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		구분당선.addSection(역삼_강남_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(StationTest.삼성역, StationTest.역삼역,
			new Sections(Stream.of(신분당선.getSections(), 구분당선.getSections())
				.flatMap(Collection::stream)
				.collect(Collectors.toList()))))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지와 도착지가 연결되지 않아 경로를 찾을 수 없습니다.");
	}

	@ParameterizedTest
	@MethodSource("findShortestPath_notExistSourceOrTarget_Exception_parameter")
	@DisplayName("존재하지 않는 출발역이나 도착역을 조회하는 경우 예외")
	void findShortestPath_notExistSourceOrTarget_Exception(Station source, Station target) {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, StationTest.삼성역, StationTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, StationTest.선릉역, StationTest.역삼역, 7);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(source, target,
			new Sections(Stream.of(신분당선.getSections(), 구분당선.getSections())
				.flatMap(Collection::stream)
				.collect(Collectors.toList()))))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지 또는 도착지가 존재하지 않아 경로를 찾을 수 없습니다.");
	}

	static Stream<Arguments> findShortestPath_notExistSourceOrTarget_Exception_parameter() {
		return Stream.of(
			Arguments.of(StationTest.삼성역, StationTest.강남역),
			Arguments.of(StationTest.강남역, StationTest.삼성역)
		);
	}
}
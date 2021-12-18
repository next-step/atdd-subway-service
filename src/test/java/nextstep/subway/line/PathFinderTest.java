package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.common.exception.BadParameterException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
	@Test
	@DisplayName("최단 경로를 조회한다.")
	void findShortestPath() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, LineTest.선릉역, LineTest.역삼역, 7);
		Section 역삼_강남_구간 = new Section(구분당선, LineTest.역삼역, LineTest.강남역, 5);
		Section 삼성_강남_구간 = new Section(구분당선, LineTest.삼성역, LineTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);
		신분당선.addSection(역삼_강남_구간);
		구분당선.addSection(삼성_강남_구간);

		PathFinder pathFinder = new PathFinder();
		GraphPath<Station, DefaultWeightedEdge> graph = pathFinder.findShortestPath(
			LineTest.삼성역, LineTest.역삼역, Arrays.asList(신분당선, 구분당선));

		assertThat(graph.getVertexList()).isEqualTo(Arrays.asList(LineTest.삼성역, LineTest.강남역, LineTest.역삼역));
		assertThat(graph.getWeight()).isEqualTo(9);
	}

	@Test
	@DisplayName("최단 경로 조회시 출발역과 도착역이 같으면 예외")
	void findShortestPath_sourceAndTargetEqual_Exception() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, LineTest.선릉역, LineTest.역삼역, 7);
		Section 역삼_강남_구간 = new Section(구분당선, LineTest.역삼역, LineTest.강남역, 5);
		Section 삼성_강남_구간 = new Section(구분당선, LineTest.삼성역, LineTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);
		신분당선.addSection(역삼_강남_구간);
		구분당선.addSection(삼성_강남_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(
			LineTest.삼성역, LineTest.삼성역, Arrays.asList(신분당선, 구분당선)))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지와 도착지가 같아 경로를 찾을 수 없습니다.");
	}

	@Test
	@DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외")
	void findShortestPath_notConnectedSourceAndDest_Exception() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 역삼_강남_구간 = new Section(구분당선, LineTest.역삼역, LineTest.강남역, 4);
		신분당선.addSection(삼성_선릉_구간);
		구분당선.addSection(역삼_강남_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(LineTest.삼성역, LineTest.역삼역,
			Arrays.asList(신분당선, 구분당선)))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지와 도착지가 연결되지 않아 경로를 찾을 수 없습니다.");
	}

	@ParameterizedTest
	@MethodSource("findShortestPath_notExistSourceOrTarget_Exception_parameter")
	@DisplayName("존재하지 않는 출발역이나 도착역을 조회하는 경우 예외")
	void findShortestPath_notExistSourceOrTarget_Exception(Station source, Station target) {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, LineTest.선릉역, LineTest.역삼역, 7);
		신분당선.addSection(삼성_선릉_구간);
		신분당선.addSection(선릉_역삼_구간);

		PathFinder pathFinder = new PathFinder();

		assertThatThrownBy(() -> pathFinder.findShortestPath(source, target,
			Arrays.asList(신분당선, 구분당선)))
			.isInstanceOf(BadParameterException.class)
			.hasMessage("출발지 또는 도착지가 존재하지 않아 경로를 찾을 수 없습니다.");
	}

	static Stream<Arguments> findShortestPath_notExistSourceOrTarget_Exception_parameter() {
		return Stream.of(
			Arguments.of(LineTest.삼성역, LineTest.강남역),
			Arguments.of(LineTest.강남역, LineTest.삼성역)
		);
	}
}
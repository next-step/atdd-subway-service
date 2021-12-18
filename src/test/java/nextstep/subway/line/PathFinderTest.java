package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
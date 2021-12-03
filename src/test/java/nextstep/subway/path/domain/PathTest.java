package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.domain.Station;

@DisplayName("지하철 경로 관련 단위 테스트")
class PathTest {

	private Station upStation1;
	private Station downStation1;
	private Station downStation2;
	private Section section1;
	private Section section2;
	private Line line1;
	private Line line2;

	@BeforeEach
	void setUp() {
		upStation1 = new Station("강남역");
		downStation1 = new Station("광교역");
		downStation2 = new Station("삼성역");
		line1 = new Line("신분당선", "red");
		line2 = new Line("이호선", "green");
		section1 = new Section(line1, upStation1, downStation1, 10);
		section2 = new Section(line2, downStation1, downStation2, 10);

		line1.addSection(section1);
		line2.addSection(section2);
	}

	@DisplayName("출발역, 종착역이 같은 경우를 판단하는 테스트")
	@Test
	void isSameSourceTargetStation() throws Exception {
		// given
		Method method = Path.class.getDeclaredMethod("isSameSourceTargetStation", Station.class, Station.class);
		method.setAccessible(true);

		// when
		boolean result = (boolean)method.invoke(new Path(), upStation1, upStation1);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("출발역, 종착역이 같은 경우 예외 발생 테스트")
	@Test
	void validSameSameSourceTargetStation() throws Exception {
		// given
		Method method = Path.class.getDeclaredMethod("validSourceTargetStation", Station.class, Station.class);
		method.setAccessible(true);

		// when // then
		assertThatThrownBy(() -> {
			method.invoke(new Path(), upStation1, upStation1);
		}).isInstanceOf(InvocationTargetException.class)
			.hasCauseInstanceOf(PathException.class);
	}

	@DisplayName("전체 지하철 노선을 이용하여 그래프를 만드는 메소드 테스트")
	@Test
	void generateWeightedMultigraphFromLines() throws Exception {
		// given
		Method method = Path.class.getDeclaredMethod("generateWeightedMultigraphFromLines", List.class);
		method.setAccessible(true);
		WeightedMultigraph<Station, DefaultWeightedEdge> graph =
			(WeightedMultigraph<Station, DefaultWeightedEdge>)method.invoke(new Path(), List.of(line1, line2));

		// then
		assertAll(
			() -> assertThat(graph.containsVertex(upStation1)).isTrue(),
			() -> assertThat(graph.containsVertex(downStation1)).isTrue(),
			() -> assertThat(graph.containsVertex(downStation2)).isTrue()
		);
	}

	@DisplayName("전체 지하철 노선의 그래프와 출발역, 종착역을 입력하여 최소 경로를 찾는 메소드 테스트")
	@Test
	void generateShortestPath() throws Exception {
		// given
		Method method = Path.class.getDeclaredMethod("generateWeightedMultigraphFromLines", List.class);
		method.setAccessible(true);
		WeightedMultigraph<Station, DefaultWeightedEdge> graph =
			(WeightedMultigraph<Station, DefaultWeightedEdge>)method.invoke(new Path(), List.of(line1, line2));

		Method generateShortestPathMethod = Path.class.getDeclaredMethod("generateShortestPath"
			, WeightedMultigraph.class
			, Station.class
			, Station.class);
		generateShortestPathMethod.setAccessible(true);

		// when
		GraphPath<Station, DefaultWeightedEdge> shortestPath =
			(GraphPath<Station, DefaultWeightedEdge>)generateShortestPathMethod.invoke(new Path()
				, graph
				, upStation1
				, downStation2);

		// then
		assertAll(
			() -> assertThat(shortestPath.getVertexList()).hasSize(3),
			() -> assertThat(shortestPath.getWeight()).isEqualTo(20)
		);
	}
}
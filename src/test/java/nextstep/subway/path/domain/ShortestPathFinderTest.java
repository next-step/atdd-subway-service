package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestPathFinderTest {
	private ShortestPathFinder shortestPathFinder;

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Line 오호선;

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Station 광화문역;
	private Station 서대문역;

	/**
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재역
	 */
	@BeforeEach
	void setUp() {
		shortestPathFinder = new ShortestPathFinder();

		신분당선 = new Line("신분당선", "red");
		이호선 = new Line("이호선", "green");
		삼호선 = new Line("삼호선", "orange");
		오호선 = new Line("오호선", "purple");

		강남역 = new Station(1L, "강남역");
		양재역 = new Station(2L, "양재역");
		교대역 = new Station(3L, "교대역");
		남부터미널역 = new Station(4L, "남부터미널역");
		광화문역 = new Station(5L, "광화문역");
		서대문역 = new Station(6L, "서대문역");

		신분당선.addSection(new Section(신분당선, 강남역, 양재역, 10));
		이호선.addSection(new Section(이호선, 교대역, 강남역, 10));
		삼호선.addSection(new Section(삼호선, 교대역, 양재역, 5));
		삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
		오호선.addSection(new Section(오호선, 광화문역, 서대문역, 5));
	}

	@Test
	@DisplayName("최단 경로를 조회한다.")
	void findShortestPathTest() {
		List<Section> sections = 신분당선.getSections();
		sections.addAll(Stream.concat(이호선.getSections().stream(), 삼호선.getSections().stream()).collect(Collectors.toList()));
		List<Station> stations = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역);
		Station sourceStation = 교대역;
		Station targetStation = 양재역;

		PathResponse response = shortestPathFinder.findPath(sections, stations, sourceStation, targetStation);
		assertThat(response.getDistance()).isEqualTo(5);
	}

	@Test
	@DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 익셉션 발생")
	void findPathValidationTest() {
		List<Section> sections = 신분당선.getSections();
		sections.addAll(Stream.concat(이호선.getSections().stream(), 삼호선.getSections().stream()).collect(Collectors.toList()));
		sections.addAll(오호선.getSections());
		List<Station> stations = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역, 광화문역, 서대문역);
		Station sourceStation = 교대역;
		Station targetStation = 광화문역;

		assertThatThrownBy(() -> shortestPathFinder.findPath(sections, stations, sourceStation, targetStation))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	void getShortestPath() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
		assertThat(dijkstraShortestPath.getPathWeight("v3", "v1")).isEqualTo(4);
	}
}
package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DisplayName("경로 찾기, 라인 그래프 생성 테스트")
class PathFinderTest {

	private Line line1;
	private Line line2;

	private Station 인천역;
	private Station 소요산역;
	private Station 시청역;
	private Station 강남역;
	private Station 주안역;
	private Station 동암역;
	private Station 부평역;
	private Station 구로역;

	@BeforeEach
	void setUp() {
		지하철_초기_데이터_생성();
	}

	@DisplayName("PathFinder 경로검색 테스트")
	@Test
	void createPathFinderTest() {
		// given
		List<Line> lines = Arrays.asList(line1, line2);
		PathFinder finder = new PathFinder(lines);

		// when
		finder.selectShortPath(인천역, 강남역);

		// then
		assertAll(
			() -> assertThat(finder.stations()).isNotNull(),
			() -> assertThat(finder.stations()).hasSize(7),
			() -> assertThat(finder.distance()).isEqualTo(Distance.of(450))
		);
	}

	@DisplayName("PathFinder 예외 케이스 테스트")
	@Test
	void exceptionPathFinderTest() {
		// given
		List<Line> lines = Arrays.asList(line1, line2);

		// when
		PathFinder finder = new PathFinder(lines);

		// then
		assertAll(
			() -> assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> finder.selectShortPath(인천역, 인천역))
		);
	}

	@DisplayName("PathFinder 객체 생성 테스트(jGraph 초기화 완료)")
	@Test
	void initPathFinderTest() {
		// given
		List<Line> lines = Arrays.asList(line1, line2);

		// when
		PathFinder finder = new PathFinder(lines);

		// then
		assertAll(
			() -> assertThat(finder).isNotNull()
		);
	}

	@DisplayName("모든 라인의 지하철 가져오기 테스트")
	@Test
	void getAllStationsTest() {
		// given
		List<Line> lines = Arrays.asList(line1, line2);

		// when
		List<Station> stations = lines.stream()
			.flatMap(it -> it.stations().stream())
			.collect(Collectors.toList());

		// then
		assertAll(
			() -> assertThat(stations).isNotNull(),
			() -> assertThat(stations).hasSize(9)
		);
	}

	@DisplayName("1호선 경로 그래프 넣기")
	@Test
	void addGraphLineTest() {
		// given
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		// when
		line1.sections()
			.forEach(section -> {
				Station upStation = section.getUpStation();
				Station downStation = section.getDownStation();
				graph.addVertex(upStation);
				graph.addVertex(downStation);
				graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.distance());
			});
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath result = dijkstraShortestPath.getPath(인천역, 소요산역);

		List<Station> shortestPath = result.getVertexList();

		// then
		System.out.println(result.getVertexList());
		System.out.println(result.getEdgeList());
		System.out.println(result.getWeight());
		System.out.println(result.getLength());
		System.out.println(result.getStartVertex());
		System.out.println(result.getEndVertex());

		assertThat(shortestPath).isNotNull();
	}

	private void 지하철_초기_데이터_생성() {
		소요산역 = 전철역_생성(1L, "소요산역");
		인천역 = 전철역_생성(2L, "인천역");
		시청역 = 전철역_생성(3L, "시청역");
		강남역 = 전철역_생성(4L, "강남역");
		주안역 = 전철역_생성(5L, "주안역");
		동암역 = 전철역_생성(6L, "동암역");
		부평역 = 전철역_생성(7L, "부평역");
		구로역 = 전철역_생성(8L, "구로역");

		line1 = 라인_생성("1호선", "blue", 인천역, 소요산역, 500);
		line2 = 라인_생성("2호선", "green", 시청역, 강남역, 300, 900);
		구간추가(line1, 인천역, 주안역, 10);
		구간추가(line1, 주안역, 동암역, 20);
		구간추가(line1, 동암역, 부평역, 30);
		구간추가(line1, 부평역, 구로역, 40);
		구간추가(line1, 구로역, 시청역, 50);
	}

	private void 구간추가(Line line, Station upStation, Station downStation, int distance) {
		line.addSection(upStation, downStation, distance);
	}

	private Station 전철역_생성(Long id, String stationName) {
		return new Station(id, stationName);
	}

	private Line 라인_생성(String name, String color, Station upStation, Station downStation, int distance) {
		return new Line(name, color, upStation, downStation, distance);
	}

	private Line 라인_생성(String name, String color, Station upStation, Station downStation, int distance, int lineFare) {
		return new Line(name, color, upStation, downStation, distance, lineFare);
	}
}

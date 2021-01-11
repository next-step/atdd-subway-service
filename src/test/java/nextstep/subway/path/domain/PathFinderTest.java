package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DisplayName("경로 찾기, 라인 그래프 생성 테스트")
@DataJpaTest
class PathFinderTest {

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;
	private LineService lineService;
	private PathService pathService;

	private LineResponse line1;
	private LineResponse line2;

	private Station 인천역;
	private Station 소요산역;

	@BeforeEach
	void setUp() {
		lineService = new LineService(lineRepository, new StationService(stationRepository));
		pathService = new PathService(lineService);

		인천역 = 전철역_생성("인천역");
		소요산역 = 전철역_생성("소요산역");

		line1 = 라인_생성("1호선", "blue", 인천역.getId(), 소요산역.getId(), 500);
		line2 = 라인_생성("2호선", "green", "시청역", "강남역", 300);
	}

	@DisplayName("모든 라인 가져오기 테스트")
	@Test
	void findAllLinesTest() {
		// given // when
		List<Line> lines = lineService.findLineAll();

		// then
		assertThat(lines).hasSize(2);
	}

	@DisplayName("1호선 경로 그래프 넣기")
	@Test
	void addGraphLineTest() {
		// given
		구간_1호선_생성();
		Line line1 = lineService.findLineAll().get(0);
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		// when
		line1.sections()
			.forEach(section -> {
				Station upStation = section.getUpStation();
				Station downStation = section.getDownStation();
				graph.addVertex(upStation);
				graph.addVertex(downStation);
				graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
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

	private void 구간_1호선_생성() {
		Line line1 = lineService.findLineAll().get(0);

		Station 주안역 = 전철역_생성("주안역");
		Station 동암역 = 전철역_생성("동암역");
		Station 부평역 = 전철역_생성("부평역");
		Station 구로역 = 전철역_생성("구로역");

		구간추가(line1, 인천역, 주안역, 10);
		구간추가(line1, 주안역, 동암역, 20);
		구간추가(line1, 동암역, 부평역, 30);
		구간추가(line1, 부평역, 구로역, 40);
	}

	private void 구간추가(Line line, Station upStation, Station downStation, int distance) {
		line.addSection(upStation, downStation, distance);
	}

	private Station 전철역_생성(String stationName) {
		return stationRepository.save(new Station(stationName));
	}

	private LineResponse 라인_생성(String name, String color, String upStationName, String downStationName, int distance) {
		Station upStation = 전철역_생성(upStationName);
		Station downStation = 전철역_생성(downStationName);
		return 라인_생성(name, color, upStation.getId(), downStation.getId(), distance);
	}

	private LineResponse 라인_생성(String name, String color, Long upId, Long downId, int distance) {
		return lineService.saveLine(new LineRequest(name, color, upId, downId, distance));
	}
}

package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;

@SpringBootTest
public class PathServiceTestForInsideInTdd {
	@Autowired
	private PathService pathService;
	@Autowired
	private LineService lineService;
	@Autowired
	private StationService stationService;
	@Autowired
	private DatabaseCleanup databaseCleanup;

	private LineResponse 신분당선;
	private LineResponse 이호선;
	private LineResponse 삼호선;
	private LineResponse 구호선;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;
	private StationResponse 남부터미널;
	private StationResponse 당산역;
	private StationResponse 여의도역;

	@BeforeEach
	public void setUp() {
		databaseCleanup.execute();
		강남역 = stationService.saveStation(new StationRequest("강남역"));
		양재역 = stationService.saveStation(new StationRequest("양재역"));
		교대역 = stationService.saveStation(new StationRequest("교대역"));
		남부터미널 = stationService.saveStation(new StationRequest("남부터미널"));
		당산역 = stationService.saveStation(new StationRequest("당산역"));
		여의도역 = stationService.saveStation(new StationRequest("여의도역"));

		신분당선 = lineService.saveLine(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
		이호선 = lineService.saveLine(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
		삼호선 = lineService.saveLine(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5));
		lineService.addLineStation(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널.getId(), 3));
		구호선 = lineService.saveLine(new LineRequest("구호선", "bg-red-600", 당산역.getId(), 여의도역.getId(), 5));
	}

	@DisplayName("최단 경로 조회")
	@Test
	void findShortestPath() {
		PathRequest pathRequest = new PathRequest(강남역.getId(), 양재역.getId());

		PathResponse pathResponse = pathService.findShortestPath(pathRequest);

		assertThat(pathResponse.getStations()).hasSize(2).contains(강남역, 양재역);
		assertThat(pathResponse.getDistance()).isEqualTo(10);
	}

	@DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenSameSourceAndTarget() {
		PathRequest pathRequest = new PathRequest(강남역.getId(), 강남역.getId());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest))
			.withMessage(PathService.SAME_SOURCE_AND_TARGET);
	}

	@DisplayName("출발역과 도착역이 연결 되어 있지 않은 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenUnconnectedSourceAndTarget() {
		PathRequest pathRequest = new PathRequest(강남역.getId(), 당산역.getId());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest))
			.withMessage(Path.UNCONNECTED_SOURCE_AND_TARGET);
	}

	@DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenNoneExistentSourceOrTarget() {
		PathRequest pathRequest = new PathRequest(강남역.getId(), 12L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest));
	}
}

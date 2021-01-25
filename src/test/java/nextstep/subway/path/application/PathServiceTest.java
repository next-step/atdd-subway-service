package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
	@InjectMocks
	private PathService pathService;
	@Mock
	private LineService lineService;
	@Mock
	private StationService stationService;

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Line 구호선;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널;
	private Station 당산역;
	private Station 여의도역;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널 = new Station("남부터미널");
		당산역 = new Station("당산역");
		여의도역 = new Station("여의도역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
		삼호선.addSection(new Section(삼호선, 교대역, 남부터미널, new Distance(3)));
		구호선 = new Line("구호선", "bg-red-600", 당산역, 여의도역, 5);
	}

	@DisplayName("최단 경로 조회")
	@Test
	void findShortestPath() {
		PathRequest pathRequest = new PathRequest(1L, 2L);

		when(stationService.findStationById(1L)).thenReturn(강남역);
		when(stationService.findStationById(2L)).thenReturn(양재역);
		when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

		PathResponse pathResponse = pathService.findShortestPath(pathRequest);

		List<StationResponse> stations = pathResponse.getStations();
		assertThat(stations).hasSize(2);
		assertThat(stations.get(0).getName()).isEqualTo(강남역.getName());
		assertThat(stations.get(1).getName()).isEqualTo(양재역.getName());
		assertThat(pathResponse.getDistance()).isEqualTo(10);
	}

	@DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenSameSourceAndTarget() {
		PathRequest pathRequest = new PathRequest(1L, 1L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest))
			.withMessage(PathService.SAME_SOURCE_AND_TARGET);
	}

	@DisplayName("출발역과 도착역이 연결 되어 있지 않은 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenUnconnectedSourceAndTarget() {
		PathRequest pathRequest = new PathRequest(1L, 2L);

		when(stationService.findStationById(1L)).thenReturn(강남역);
		when(stationService.findStationById(2L)).thenReturn(당산역);
		when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest))
			.withMessage(Path.UNCONNECTED_SOURCE_AND_TARGET);
	}

	@DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 IllegalArgumentException 발생")
	@Test
	void findShortestPathWhenNoneExistentSourceOrTarget() {
		PathRequest pathRequest = new PathRequest(1L, 2L);

		when(stationService.findStationById(1L)).thenReturn(강남역);
		when(stationService.findStationById(2L)).thenThrow(new IllegalArgumentException());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> pathService.findShortestPath(pathRequest));
	}
}

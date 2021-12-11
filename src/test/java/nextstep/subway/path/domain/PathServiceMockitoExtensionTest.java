package nextstep.subway.path.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("MockitoExtension을 사용한 PathServiceTest")
@ExtendWith(MockitoExtension.class)
class PathServiceMockitoExtensionTest {
	private static Station 교대역 = new Station(1L, "교대역");
	private static Station 강남역 = new Station(2L, "강남역");
	private static Station 선릉역 = new Station(3L, "선릉역");
	private static Station 양재역 = new Station(4L, "양재역");
	private static Station 도곡역 = new Station(5L, "도곡역");
	private static Station 천안역 = new Station(6L, "천안역");

	private static Line 이호선 = new Line("이호선", "green", 교대역, 선릉역, Distance.of(20));
	private static Line 삼호선 = new Line("삼호선", "orange", 교대역, 도곡역, Distance.of(11));
	private static Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, Distance.of(1));
	private static Line 수인분당선 = new Line("수인분당선", "yellow", 선릉역, 도곡역, Distance.of(20));

	private static Section 이호선_추가구간 = new Section(null, 이호선, 교대역, 강남역, Distance.of(10));
	private static Section 삼호선_추가구간 = new Section(null, 삼호선, 교대역, 양재역, Distance.of(1));

	static {
		이호선.addSection(이호선_추가구간);
		삼호선.addSection(삼호선_추가구간);
	}

	@Mock
	private StationService stationService;

	@Mock
	private LineService lineService;

	@Test
	@DisplayName("경로 조회 성공")
	public void findPathSuccessTest() {
		//given
		when(lineService.findAll()).thenReturn(Lists.newArrayList(이호선, 삼호선, 신분당선, 수인분당선));
		when(stationService.findAll()).thenReturn(Lists.newArrayList(교대역, 강남역, 선릉역, 양재역, 도곡역, 천안역));
		when(stationService.findById(1L)).thenReturn(교대역);
		when(stationService.findById(3L)).thenReturn(선릉역);
		PathService pathService = new PathService(stationService, lineService);
		//when
		PathResponse pathResponse = pathService.findPaths(1, 3);
		List<Long> pathIds = pathResponse.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		//then
		assertThat(pathIds).containsExactly(1L, 4L, 2L, 3L);
		assertThat(pathResponse.getDistance()).isEqualTo(12);
	}

	@Test
	@DisplayName("출발역과 도착역이 같아서 경로 조회 실패")
	public void findPathFailStartStationEqualsEndStationTest() {
		//given
		PathService pathService = new PathService(stationService, lineService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(3, 3))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역이 동일하면 안 됩니다.");
	}

	@Test
	@DisplayName("출발역과 도착역이 존재하지 않아서 경로 조회 실패")
	public void findPathFailNoneStationTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(교대역, 강남역, 선릉역, 양재역, 도곡역, 천안역));
		PathService pathService = new PathService(stationService, lineService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(3, 7))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역 모두 존재해야 합니다.");
	}

	@Test
	@DisplayName("역이 연결되어 있지 않아서 경로 조회 실패")
	public void findPathFailNotConnectedStationTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(교대역, 강남역, 선릉역, 양재역, 도곡역, 천안역));
		when(lineService.findAll()).thenReturn(Lists.newArrayList(이호선, 삼호선, 신분당선, 수인분당선));
		PathService pathService = new PathService(stationService, lineService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(1, 6))
			.isInstanceOf(IllegalArgumentException.class);
	}

}


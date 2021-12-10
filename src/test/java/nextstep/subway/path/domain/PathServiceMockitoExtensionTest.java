package nextstep.subway.path.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Distance;
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
	private static Station 강남역 = new Station(1L, "강남역");
	private static Station 역삼역 = new Station(2L, "역삼역");
	private static Station 선릉역 = new Station(3L, "선릉역");
	private static Station 천안역 = new Station(4L, "천안역");
	private static Section 첫번째_구간 = new Section(null, null, 강남역, 역삼역, Distance.of(10));
	private static Section 두번째_구간 = new Section(null, null, 역삼역, 선릉역, Distance.of(10));
	private static Section 세번째_구간 = new Section(null, null, 강남역, 선릉역, Distance.of(100));

	@Mock
	private StationService stationService;

	@Mock
	private SectionService sectionService;

	@Test
	@DisplayName("경로 조회 성공")
	public void findPathSuccessTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(강남역, 역삼역, 선릉역));
		when(sectionService.findAll()).thenReturn(Lists.newArrayList(첫번째_구간, 두번째_구간, 세번째_구간));
		when(stationService.findById(1L)).thenReturn(강남역);
		when(stationService.findById(2L)).thenReturn(역삼역);
		when(stationService.findById(3L)).thenReturn(선릉역);
		PathService pathService = new PathService(stationService, sectionService);
		//when
		PathResponse pathResponse = pathService.findPaths(3, 1);
		List<Long> pathIds = pathResponse.getStations()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		//then
		assertThat(pathIds).containsExactly(3L, 2L, 1L);
		assertThat(pathResponse.getDistance()).isEqualTo(20);
	}

	@Test
	@DisplayName("출발역과 도착역이 같아서 경로 조회 실패")
	public void findPathFailStartStationEqualsEndStationTest() {
		//given
		PathService pathService = new PathService(stationService, sectionService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(3, 3))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역이 동일하면 안 됩니다.");
	}

	@Test
	@DisplayName("출발역과 도착역이 존재하지 않아서 경로 조회 실패")
	public void findPathFailNoneStationTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(강남역, 역삼역, 선릉역));
		PathService pathService = new PathService(stationService, sectionService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(3, 7))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역 모두 존재해야 합니다.");
	}

	@Test
	@DisplayName("역이 연결되어 있지 않아서 경로 조회 실패")
	public void findPathFailNotConnectedStationTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(강남역, 역삼역, 선릉역, 천안역));
		when(sectionService.findAll()).thenReturn(Lists.newArrayList(첫번째_구간, 두번째_구간, 세번째_구간));
		PathService pathService = new PathService(stationService, sectionService);

		//when
		assertThatThrownBy(() -> pathService.findPaths(3, 4))
			.isInstanceOf(NullPointerException.class);
	}

}


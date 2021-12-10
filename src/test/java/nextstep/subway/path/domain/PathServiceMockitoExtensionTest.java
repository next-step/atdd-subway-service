package nextstep.subway.path.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
	private static Section 첫번째_구간 = new Section(null, null, 강남역, 역삼역, Distance.of(10));
	private static Section 두번째_구간 = new Section(null, null, 역삼역, 선릉역, Distance.of(10));
	private static Section 세번째_구간 = new Section(null, null, 강남역, 선릉역, Distance.of(100));

	@Mock
	private StationService stationService;

	@Mock
	private SectionService sectionService;

	@Test
	@DisplayName("경로 조회 ")
	public void findPathTest() {
		//given
		when(stationService.findAll()).thenReturn(Lists.newArrayList(강남역, 역삼역, 선릉역));
		when(sectionService.findAll()).thenReturn(Lists.newArrayList(첫번째_구간, 두번째_구간, 세번째_구간));
		when(stationService.findById(1L)).thenReturn(강남역);
		when(stationService.findById(2L)).thenReturn(역삼역);
		when(stationService.findById(3L)).thenReturn(선릉역);
		PathService pathService = new PathService(stationService, sectionService);

		//when
		PathResponse pathResponse = pathService.findPaths(3, 1);
		List<Long> pathIds = pathResponse.getStationResponses()
			.stream()
			.map(StationResponse::getId)
			.collect(Collectors.toList());

		//then
		assertThat(pathIds).containsExactly(3L, 2L, 1L);
		assertThat(pathResponse.getDistance()).isEqualTo(20);
	}

}


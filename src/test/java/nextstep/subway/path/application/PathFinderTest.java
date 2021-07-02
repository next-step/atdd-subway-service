package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.FindPathValidator;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PathFinderTest {
	@Mock
	private SectionRepository sectionRepository;
	@Mock
	private StationRepository stationRepository;
	@Mock
	private FindPathValidator findPathValidator;
	@Mock
	private ShortestPathFinder shortestPathFinder;

	@Test
	@DisplayName("최단 경로 조회 요청")
	void findPathTest() {
		when(stationRepository.findAll()).thenReturn(Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "양재역")));
		when(sectionRepository.findAll()).thenReturn(Lists.newArrayList(new Section()));
		when(shortestPathFinder.findPath(any(), any(), any(), any())).thenReturn(new PathResponse(Lists.newArrayList(new StationResponse()), 5));

		PathFinder pathFinder = new PathFinder(sectionRepository, stationRepository, findPathValidator, shortestPathFinder);
		PathResponse response = pathFinder.findPath(1L, 2L);
		assertThat(response.getStations()).hasSize(1);
	}
}

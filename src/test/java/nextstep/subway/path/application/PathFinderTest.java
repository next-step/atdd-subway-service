package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.FindPathValidator;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PathFinderTest {
	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationService stationService;
	@Mock
	private FindPathValidator findPathValidator;
	@Mock
	private ShortestPathFinder shortestPathFinder;

	@Test
	@DisplayName("최단 경로 조회 요청")
	void findPathTest() {
		when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
		when(stationService.findById(any())).thenReturn(new Station());
		when(shortestPathFinder.findPath(any(), any(), any())).thenReturn(new PathResponse(Lists.newArrayList(new StationResponse()), 5));

		PathFinder pathFinder = new PathFinder(lineRepository, stationService, findPathValidator, shortestPathFinder);
		PathResponse response = pathFinder.findPath(1L, 2L);
		assertThat(response.getStations()).hasSize(1);
	}

	@Test
	@DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 익셉션 발생")
	void findPathValidationTest2() {
		when(stationService.findById(any())).thenThrow(RuntimeException.class);

		PathFinder pathFinder = new PathFinder(lineRepository, stationService, findPathValidator, shortestPathFinder);
		assertThatThrownBy(() -> pathFinder.findPath(1L, 3L))
				.isInstanceOf(RuntimeException.class);
	}
}

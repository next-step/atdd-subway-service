package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@DisplayName("First Outside In Test")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	@Mock
	private LineService lineService;
	@Mock
	private StationService stationService;

	private PathService pathService;

	private Line line;
	private Station 시청역;
	private Station 서초역;

	@BeforeEach
	void setUp() {
		pathService = new PathService(stationService);

		시청역 = new Station(1L, "시청역");
		서초역 = new Station(2L, "서초역");

		line = new Line(1L, "2호선", "green", 시청역, 서초역, 100);
	}

	@DisplayName("경로찾기: [에러]동일한 역 경로검색")
	@Test
	void findPathSameStationTest() {
		// given
		when(stationService.findStationById(1L)).thenReturn(시청역);
		PathRequest request = new PathRequest(시청역.getId(), 시청역.getId());

		// when // then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> pathService.findPath(request));
	}
}

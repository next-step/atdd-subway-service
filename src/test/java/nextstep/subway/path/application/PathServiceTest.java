package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(SpringExtension.class)
public class PathServiceTest {
	@MockBean
	private LineRepository lineRepository;

	@MockBean
	private StationService stationService;

	@Test
	void findPathBetweenTwoStationTest() {
		// given
		when(lineRepository.findAll()).thenReturn(getLines());
		when(stationService.findStationById(1L)).thenReturn(new Station(1L, "신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station(2L, "홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(7);
	}

	private List<Line> getLines() {
		Station station1 = new Station(1L, "신촌역");
		Station station2 = new Station(2L, "홍대입구역");
		Line line = new Line("2호선", "green", station1, station2, 7);

		return Arrays.asList(line);
	}
}

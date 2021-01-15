package nextstep.subway.path.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.dto.StationResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
public class PathControllerTest {
	@Mock
	private PathService pathService;

	@Test
	void findShortestPath() {
		//given
		Long sourceId = 1L;
		Long targetId = 1L;

		PathResponse mockPathResponse = PathResponse.builder()
			.stations(Arrays.asList(new PathStationResponse(1L, "교대역", LocalDateTime.now()),
				new PathStationResponse(2L, "고속터미널역", LocalDateTime.now())))
			.distance(3)
			.build();

		when(pathService.findShortestPath(sourceId, targetId)).thenReturn(mockPathResponse);
		PathController pathController = new PathController(pathService);

		// when
		ResponseEntity response = pathController.findShortestPath(sourceId, targetId);

		//then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}
}

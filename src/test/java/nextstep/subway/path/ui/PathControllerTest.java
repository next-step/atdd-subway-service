package nextstep.subway.path.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.common.exception.DuplicateSourceAndTargetException;
import nextstep.subway.common.exception.NoSourceStationException;
import nextstep.subway.common.exception.NoTargetStationException;
import nextstep.subway.common.exception.NotConnectedLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.domain.Station;

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
		Long targetId = 2L;

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

	@DisplayName("출발역과 도착역이 같은 경우")
	@Test
	void sameSourceAndTargetException() {
		//given
		Long sourceId = 1L;
		Long targetId = 1L;

		// when
		when(pathService.findShortestPath(sourceId, targetId)).thenThrow(new DuplicateSourceAndTargetException());
		PathController pathController = new PathController(pathService);

		// then
		assertThatThrownBy(() -> {
			pathController.findShortestPath(sourceId, targetId);

		}).isInstanceOf(DuplicateSourceAndTargetException.class);
	}

	@DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
	@Test
	void sourceStationAndTargetStationNotConnectedException() {
		//given
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		Station 교대역 = new Station(3L, "교대역");
		Station 남부터미널역 = new Station(4L, "남부터미널역");

		Line 신분당선 = new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 이호선 = new Line(12L, "이호선", "bg-red-600", 교대역, 강남역, 10);

		//when
		when(pathService.findShortestPath(강남역.getId(), 남부터미널역.getId())).thenThrow(new NotConnectedLineException());
		PathController pathController = new PathController(pathService);

		// then
		assertThatThrownBy(() -> {
			pathController.findShortestPath(강남역.getId(), 남부터미널역.getId());

		}).isInstanceOf(NotConnectedLineException.class);

	}

	@DisplayName("존재하지 않은 출발역을 조회할 경우")
	@Test
	void noSourceStationException() {
		//given
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		Station 교대역 = new Station(3L, "교대역");
		Station 남부터미널역 = new Station(4L, "남부터미널역");

		Line 신분당선 = new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 이호선 = new Line(12L, "이호선", "bg-red-600", 교대역, 강남역, 10);

		Long invalidSourceId = 500L;
		//when
		when(pathService.findShortestPath(invalidSourceId, 남부터미널역.getId())).thenThrow(new NoSourceStationException());
		PathController pathController = new PathController(pathService);

		// then
		assertThatThrownBy(() -> {
			pathController.findShortestPath(invalidSourceId, 남부터미널역.getId());

		}).isInstanceOf(NoSourceStationException.class);

	}

	@DisplayName("존재하지 않은 도착역을 조회할 경우")
	@Test
	void noTargetStationException() {
		//given
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		Station 교대역 = new Station(3L, "교대역");
		Station 남부터미널역 = new Station(4L, "남부터미널역");

		Line 신분당선 = new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
		Line 이호선 = new Line(12L, "이호선", "bg-red-600", 교대역, 강남역, 10);

		Long invalidSourceId = 500L;
		//when
		when(pathService.findShortestPath(강남역.getId(), invalidSourceId)).thenThrow(new NoTargetStationException());
		PathController pathController = new PathController(pathService);

		// then
		assertThatThrownBy(() -> {
			pathController.findShortestPath(강남역.getId(), invalidSourceId);

		}).isInstanceOf(NoTargetStationException.class);

	}
}

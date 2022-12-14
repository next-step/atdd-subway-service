package nextstep.subway.path.application;

import static nextstep.subway.generator.LineGenerator.*;
import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Name;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.application.StationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("경로 서비스")
class PathServiceTest {

	@Mock
	private LineService lineService;

	@Mock
	private StationService stationService;

	@InjectMocks
	private PathService pathService;

	@Test
	@DisplayName("최단 경로를 조회한다")
	void findShortestPath() {
		// given
		Long 출발지 = 1L;
		Long 도착지 = 2L;
		when(lineService.findAll()).thenReturn(Lines.from(Collections.singletonList(삼호선())));
		when(stationService.findById(출발지)).thenReturn(station(Name.from("교대역")));
		when(stationService.findById(도착지)).thenReturn(station(Name.from("양재역")));

		// when
		PathResponse response = pathService.findShortestPath(LoginMember.guest(), new PathRequest(출발지, 도착지));

		// then
		assertAll(
			() -> assertThat(response.getDistance()).isEqualTo(15),
			() -> assertThat(response.getStations()).hasSize(2)
				.extracting(PathStationResponse::getName)
				.containsExactly("교대역", "양재역")
		);
	}

	private Line 삼호선() {
		return line("삼호선", "orange", "교대역", "양재역" ,15);
	}

}
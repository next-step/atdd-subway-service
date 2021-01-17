package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.PathStationResponse;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

	@Mock
	LineRepository lineRepository;
	@Mock
	StationRepository stationRepository;

	@InjectMocks
	PathService pathService;

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	/**
	 * 교대역    ---   *2호선* (10)  ---   강남역
	 * |                                  |
	 * *3호선* (3)                     *신분당선* (10)
	 * |                                  |
	 * 남부터미널역  ---  *3호선* (2)   ---   양재
	 */

	@BeforeEach
	public void setUp() {
		강남역 = new Station(1L, "강남역");
		양재역 = new Station(2L, "양재역");
		교대역 = new Station(3L, "교대역");
		남부터미널역 = new Station(4L, "남부터미널역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

		삼호선.addSection(교대역, 남부터미널역, 3);
	}

	@Test
	@DisplayName("최단거리를 구하면 최단거리 구간의 역 목록과 최단거리가 계산되어야 한다.")
	void shortestPath() {
		//given
		when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
		when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
		when(stationRepository.findById(양재역.getId())).thenReturn(Optional.of(양재역));

		//when
		PathResponse response = pathService.findShortestPath(new PathRequest(교대역.getId(), 양재역.getId()));

		//then
		assertThat(response.getStations()).containsExactly(PathStationResponse.of(교대역),
			PathStationResponse.of(남부터미널역),
			PathStationResponse.of(양재역));
		assertThat(response.getDistance()).isEqualTo(5L);
		assertThat(response.getFare()).isEqualTo(1250);
	}

	@Test
	@DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException을 throw 해야한다.")
	void sameSourceAndTarget() {
		//when/then
		assertThatThrownBy(() -> pathService.findShortestPath(new PathRequest(1L, 1L)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하지 않은 출발역을 조회 할 경우 IllegalArgumentException을 throw 해야한다.")
	void notExistSource() {
		//given
		Long 없는_출발역ID = 100L;
		when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
		when(stationRepository.findById(없는_출발역ID)).thenReturn(Optional.empty());

		//when/then
		assertThatThrownBy(() -> pathService.findShortestPath(new PathRequest(없는_출발역ID, 양재역.getId())))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("존재하지 않은 도착역을 조회 할 경우 IllegalArgumentException을 throw 해야한다.")
	void notExistTarget() {
		//given
		Long 없는_도착역ID = 100L;
		when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
		when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
		when(stationRepository.findById(없는_도착역ID)).thenReturn(Optional.empty());

		//when/then
		assertThatThrownBy(() -> pathService.findShortestPath(new PathRequest(교대역.getId(), 없는_도착역ID)))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

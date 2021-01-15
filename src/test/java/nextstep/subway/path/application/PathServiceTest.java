package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;
	@Mock
	private PathFinder pathFinder;

	private Station 강남역 = new Station(1L, "강남역");
	private Station 양재역 = new Station(2L, "양재역");
	private Station 교대역 = new Station(3L, "교대역");
	private Station 남부터미널역 = new Station(4L, "남부터미널역");

	private Line 신분당선 = new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10);
	private Line 이호선 = new Line(12L, "이호선", "bg-red-600", 교대역, 강남역, 10);
	private Line 삼호선 = new Line(13L, "삼호선", "bg-red-600", 교대역, 양재역, 5);

	/**
	 * 교대역    --- *2호선(10)* ---   강남역
	 * |                        |
	 * *3호선(3)*                   *신분당선(10)*
	 * |                        |
	 * 남부터미널역  --- *3호선(2)* ---   양재
	 */
	@BeforeEach
	public void setUp() {
		삼호선.addSection(교대역, 남부터미널역, 3);
	}

	@Test
	void findShortestPath() {
		//given
		Long sourceId = 교대역.getId();
		Long targetId = 양재역.getId();

		List<Line> 신분당선_mock = Arrays.asList(신분당선, 이호선, 삼호선);
		Optional<Station> 교대역_mock = Optional.of(교대역);
		Optional<Station> 양재역_mock = Optional.of(양재역);

		List<PathStationResponse> pathStationResponses = Arrays.asList(교대역, 남부터미널역, 양재역).stream()
			.map(station -> PathStationResponse.of(station))
			.collect(Collectors.toList());
		int distance_mock = 5;

		when(lineRepository.findAll()).thenReturn(신분당선_mock);
		when(stationRepository.findById(sourceId)).thenReturn(교대역_mock);
		when(stationRepository.findById(targetId)).thenReturn(양재역_mock);
		when(pathFinder.getDijkstraShortestPath(신분당선_mock, 교대역_mock.get(), 양재역_mock.get())).thenReturn(
			PathResponse.of(pathStationResponses, distance_mock));

		// when
		PathService pathService = new PathService(lineRepository, stationRepository, pathFinder);
		PathResponse pathResponse = pathService.findShortestPath(sourceId, targetId);

		// then
		assertThat(pathResponse.getStations()).containsExactly(PathStationResponse.of(교대역),
			PathStationResponse.of(남부터미널역), PathStationResponse.of(양재역));
		assertThat(pathResponse.getDistance()).isEqualTo(distance_mock);
	}

}
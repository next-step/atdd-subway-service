package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	private List<Line> lines;
	private Station 교대역;
	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 석촌역;
	private Station 송파역;
	private Station 잠실역;

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Line 팔호선;

	@Mock
	private LineRepository lineRepository;

	@Mock
	private StationService stationService;

	@BeforeEach
	void setUp() {
		교대역 = new Station(1L, "교대역");
		강남역 = new Station(2L, "강남역");
		남부터미널역 = new Station(3L, "남부터미널역");
		양재역 = new Station( 4L, "양재역");
		석촌역 = new Station(5L, "석촌역");
		송파역 = new Station(6L, "송파역");
		잠실역 = new Station(7L, "잠실역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
		팔호선 = new Line("팔호선", "bg-red-600", 석촌역, 송파역, 5);
		삼호선.addSection(교대역, 남부터미널역, 3);

		lines = new ArrayList<>();
		lines.add(신분당선);
		lines.add(이호선);
		lines.add(삼호선);
		lines.add(팔호선);
	}

	@Test
	void findPath() {
		// given
		MockedStatic<PathFinder> mockedPathFinder = mockStatic(PathFinder.class);
		mockedPathFinder.when(() -> PathFinder.findPath(lines, 교대역, 양재역))
			.thenReturn(Optional.ofNullable(new Path(Arrays.asList(교대역, 남부터미널역, 양재역), 5L)));
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findById(교대역.getId())).thenReturn(교대역);
		when(stationService.findById(양재역.getId())).thenReturn(양재역);

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId());
		mockedPathFinder.close();

		// then
		assertThat(response.getStations())
			.map(PathStationResponse::getId)
			.containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
		assertThat(response.getStations())
			.map(PathStationResponse::getName)
			.containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName());
		assertThat(response.getDistance()).isEqualTo(5L);
	}
}

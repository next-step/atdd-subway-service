package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.FareCalculater;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	private final Station 교대역 = new Station(1L, "교대역");
	private final Station 남부터미널역 = new Station(2L, "남부터미널역");
	private final Station 양재역 = new Station(3L, "양재역");

	private final Line 신분당선 = new Line("신분당선", "bg-red-600", 10);
	private final Line 이호선 = new Line("이호선", "bg-red-600", 20);
	private final Line 삼호선 = new Line("삼호선", "bg-red-600", 30);
	private final Line 팔호선 = new Line("팔호선", "bg-red-600");

	private List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선, 팔호선);

	@Mock
	private LineRepository lineRepository;

	@Mock
	private StationService stationService;

	@Mock
	private FareCalculater fareCalculater;

	@InjectMocks
	private PathService pathService;

	@Test
	void findPath() {
		try (MockedStatic<PathFinder> mock = mockStatic(PathFinder.class, this::answerPathFinder)) {
			// given
			when(lineRepository.findAll()).thenReturn(lines);
			when(stationService.findById(교대역.getId())).thenReturn(교대역);
			when(stationService.findById(양재역.getId())).thenReturn(양재역);
			when(fareCalculater.calculateFare(any(), any())).thenReturn(Fare.from(0));

			// when
			PathResponse response = pathService.findPath(new LoginMember(), 교대역.getId(), 양재역.getId());

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

	public Optional<Path> answerPathFinder(InvocationOnMock invocation) {
		if(invocation.getMethod().getName().equals("findPath")
			&& invocation.getArguments().length == 3
			&& invocation.getArgument(1).equals(교대역)
			&& invocation.getArgument(2).equals(양재역)) {

			Sections sections = new Sections();
			sections.addSection(삼호선, 교대역, 양재역, 5);
			sections.addSection(삼호선, 교대역, 남부터미널역, 3);

			return Optional.of( new Path(sections, Arrays.asList(교대역, 남부터미널역, 양재역), 5L));
		}
		return Optional.empty();
	}
}

package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(SpringExtension.class)
public class PathServiceTest {
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private Line line3;
	private Line lineBundang;
	private Line line2;
	private List<Line> lines;

	@MockBean
	private LineRepository lineRepository;

	@MockBean
	private StationService stationService;

	@BeforeEach
	void setUp() {
		station1 = new Station("교대역");
		station2 = new Station("양재역");
		station3 = new Station("강남역");
		station4 = new Station("남부터미널역");
		line3 = new Line("3호선", "orange", 700);
		line3.addLineStation(station1, station4, 3);
		line3.addLineStation(station4, station2, 2);
		lineBundang = new Line("신분당선", "pink", station2, station3, 10);
		line2 = new Line("2호선", "green", station1, station3, 5, 1000);
		lines = Arrays.asList(line2, line3, lineBundang);
	}

	@Test
	void calculateLineAdditionalFareWithNoTransferAndLogIn() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when, then
		assertThatThrownBy(() -> pathService.findPath(new LoginMember(), 2L, 4L)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void calculateLineAdditionalFareWithNoTransferAndLogInWhoNoneOfTargets() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 3), 2L, 4L);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(8);
		assertThat(pathResponse.getFare()).isEqualTo(0);
	}

	@Test
	void calculateLineAdditionalFareWithNoTransferAndLogInWhoIsAdult() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 30), 2L, 4L);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(8);
		assertThat(pathResponse.getFare()).isEqualTo(2250);
	}

	@Test
	void calculateLineAdditionalFareWithNoTransferAndLogInWhoIsAdolescent() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 15), 2L, 4L);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(8);
		assertThat(pathResponse.getFare()).isEqualTo(1870);
	}

	@Test
	void calculateLineAdditionalFareWithNoTransferAndLogInWhoIssChild() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 8), 2L, 4L);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(8);
		assertThat(pathResponse.getFare()).isEqualTo(1300);
	}

	@Test
	void calculateLineAdditionalFareTest() {
		// given
		when(lineRepository.findAll()).thenReturn(lines);
		when(stationService.findStationById(1L)).thenReturn(new Station("교대역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("남부터미널역"));
		when(stationService.findStationById(3L)).thenReturn(new Station("양재역"));
		when(stationService.findStationById(4L)).thenReturn(new Station("강남역"));
		when(stationService.findByName("교대역")).thenReturn(new Station("교대역"));
		when(stationService.findByName("남부터미널역")).thenReturn(new Station("남부터미널역"));
		when(stationService.findByName("양재역")).thenReturn(new Station("양재역"));
		when(stationService.findByName("강남역")).thenReturn(new Station("강남역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25), 2L, 4L);
		List<String> stationNames = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationNames = Arrays.asList("남부터미널역", "교대역", "강남역");

		assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
		assertThat(pathResponse.getDistance()).isEqualTo(8);
		assertThat(pathResponse.getFare()).isEqualTo(2250);
	}

	@Test
	void calculateFareBetweenTwoStationTest() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations(10));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25),1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(10);
		assertThat(pathResponse.getFare()).isEqualTo(1250);
	}

	@Test
	void calculateFareBetweenTwoStationTestOn170Kilometer() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations(170));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25),1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(170);
		assertThat(pathResponse.getFare()).isEqualTo(3550);
	}

	@Test
	void calculateFareBetweenTwoStationTestOn57Kilometer() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations(57));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25),1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(57);
		assertThat(pathResponse.getFare()).isEqualTo(2150);
	}

	@Test
	void calculateFareBetweenTwoStationTestOn39Kilometer() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations(39));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25),1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(39);
		assertThat(pathResponse.getFare()).isEqualTo(1850);
	}

	@Test
	void calculateFareBetweenTwoStationTestOn15Kilometer() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations(15));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25),1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(15);
		assertThat(pathResponse.getFare()).isEqualTo(1350);
	}

	@Test
	void findPathBetweenTwoStationTest() {
		// given
		when(lineRepository.findAll()).thenReturn(getLineThatHasTwoStations());
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		when(stationService.findByName("신촌역")).thenReturn(new Station("신촌역"));
		when(stationService.findByName("홍대입구역")).thenReturn(new Station("홍대입구역"));

		PathService pathService = new PathService(lineRepository, stationService);

		// when
		PathResponse pathResponse = pathService.findPath(new LoginMember(1L, "email@email.com", 25), 1L, 2L);
		List<String> stationIds = pathResponse.getStations().stream()
			.map(it -> it.getName())
			.collect(Collectors.toList());

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathResponse.getDistance()).isEqualTo(7);
	}

	private List<Line> getLineThatHasTwoStations(int distance) {
		Station station1 = new Station("신촌역");
		Station station2 = new Station("홍대입구역");
		Line line = new Line("2호선", "green", station1, station2, distance);

		return Arrays.asList(line);
	}

	private List<Line> getLineThatHasTwoStations() {
		Station station1 = new Station("신촌역");
		Station station2 = new Station("홍대입구역");
		Line line = new Line("2호선", "green", station1, station2, 7);

		return Arrays.asList(line);
	}
}

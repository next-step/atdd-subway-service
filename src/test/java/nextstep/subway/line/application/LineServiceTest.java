package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(SpringExtension.class)
class LineServiceTest {

	@MockBean
	private LineRepository lineRepository;

	@MockBean
	private StationService stationService;

	private Station 강남역;
	private Station 광교역;
	private Station 양재역;
	private Line line;

	@BeforeEach
	void setUp() {
		this.강남역 = new Station("강남역");
		this.광교역 = new Station("광교역");
		this.양재역 = new Station("양재역");
		this.line = new Line("신분당선", "red", this.강남역, this.광교역, 10);
		this.line.addSection(양재역, 강남역, 10);
	}

	@Test
	void testSaveLine() {
		LineService lineService = new LineService(this.lineRepository, this.stationService);
		LineRequest lineRequest = new LineRequest("신분당선", "red", 1L, 2L, 10, 0);

		Mockito.when(this.stationService.findById(Mockito.eq(1L))).thenReturn(this.강남역);
		Mockito.when(this.stationService.findById(Mockito.eq(2L))).thenReturn(this.광교역);
		Mockito.when(this.lineRepository.save(Mockito.any())).thenReturn(line);

		LineResponse actual = lineService.saveLine(lineRequest);

		assertAll(() -> {
			assertThat(actual.getName()).isEqualTo("신분당선");
			assertThat(actual.getColor()).isEqualTo("red");
			assertThat(getStationNames(actual)).containsExactly("양재역", "강남역", "광교역");
		});
	}

	@Test
	void testAddSection() {
		LineService lineService = new LineService(this.lineRepository, this.stationService);
		SectionRequest sectionRequest = new SectionRequest(3L, 2L, 5);

		Station 정자역 = new Station("정자역");

		Mockito.when(this.lineRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(this.line));
		Mockito.when(this.stationService.findStationById(Mockito.eq(3L))).thenReturn(정자역);
		Mockito.when(this.stationService.findStationById(Mockito.eq(2L))).thenReturn(this.광교역);
		lineService.addLineStation(1L, sectionRequest);

		assertThat(this.line.getStations()).containsExactly(양재역, 강남역, 정자역, 광교역);
	}

	@Test
	void testRemoveSection() {
		LineService lineService = new LineService(this.lineRepository, this.stationService);

		Mockito.when(this.lineRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(this.line));
		Mockito.when(this.stationService.findStationById(Mockito.eq(1L))).thenReturn(강남역);

		lineService.removeLineStation(1L, 1L);
		assertThat(line.getStations()).containsExactly(양재역, 광교역);
	}

	private Stream<String> getStationNames(LineResponse actual) {
		return actual.getStations().stream().map(StationResponse::getName);
	}
}

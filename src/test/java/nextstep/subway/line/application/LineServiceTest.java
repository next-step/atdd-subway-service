package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(SpringExtension.class)
class LineServiceTest {

	@MockBean
	private LineRepository lineRepository;

	@MockBean
	private StationService stationService;

	private Station upStation;
	private Station downStation;
	private Line line;

	@BeforeEach
	void setUp() {
		this.upStation = new Station("강남역");
		this.downStation = new Station("광교역");
		this.line = new Line("신분당선", "red", upStation, downStation, 10);
	}

	@Test
	void testSaveLine() {
		LineService lineService = new LineService(this.lineRepository, this.stationService);
		LineRequest lineRequest = new LineRequest("신분당선", "red", 1L, 2L, 10);

		Mockito.when(this.stationService.findById(Mockito.eq(1L))).thenReturn(upStation);
		Mockito.when(this.stationService.findById(Mockito.eq(2L))).thenReturn(downStation);
		Mockito.when(this.lineRepository.save(Mockito.any())).thenReturn(line);

		LineResponse actual = lineService.saveLine(lineRequest);

		assertAll(() -> {
			assertThat(actual.getName()).isEqualTo("신분당선");
			assertThat(actual.getColor()).isEqualTo("red");
			assertThat(getStationNames(actual)).containsExactly("강남역", "광교역");
		});
	}

	private Stream<String> getStationNames(LineResponse actual) {
		return actual.getStations().stream().map(StationResponse::getName);
	}
}

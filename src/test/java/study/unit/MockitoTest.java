package study.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@DisplayName("단위 테스트 - mockito를 활용한 가짜 협력 객체 사용")
public class MockitoTest {

	private Line line;
	private Station 시청역;
	private Station 서초역;

	@BeforeEach
	void setUp() {
		시청역 = new Station(1L, "시청역");
		서초역 = new Station(2L, "서초역");

		line = new Line(1L, "2호선", "green", 시청역, 서초역, 100);
	}

	@Test
	void findAllLines() {
		// given
		LineRepository lineRepository = mock(LineRepository.class);
		StationService stationService = mock(StationService.class);

		when(lineRepository.findAll()).thenReturn(Lists.newArrayList(line));
		LineService lineService = new LineService(lineRepository, stationService);

		// when
		List<LineResponse> responses = lineService.findLines();

		// then
		assertThat(responses).hasSize(1);
	}
}

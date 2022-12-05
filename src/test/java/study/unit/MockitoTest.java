package study.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import nextstep.subway.generator.LineGenerator;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;

@DisplayName("단위 테스트 - mockito를 활용한 가짜 협력 객체 사용")
class MockitoTest {
	@Test
	void findAllLines() {
		// given
		LineRepository lineRepository = mock(LineRepository.class);
		StationService stationService = mock(StationService.class);
		SectionService sectionService = mock(SectionService.class);

		when(lineRepository.findAll()).thenReturn(Lists.newArrayList(이호선()));
		LineService lineService = new LineService(lineRepository, stationService, sectionService);

		// when
		List<LineResponse> responses = lineService.findLines();

		// then
		assertThat(responses).hasSize(1);
	}

	private Line 이호선() {
		return LineGenerator.line("이호선", "green", "강남역", "역삼역", 10);
	}
}

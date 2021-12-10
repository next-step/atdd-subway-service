package study.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.google.common.collect.Lists;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class SpringExtensionTest {
	@MockBean
	private LineRepository lineRepository;
	@MockBean
	private StationService stationService;

	@Test
	void findAllLines() {
		// given
		when(lineRepository.findAll()).thenReturn(Lists.newArrayList(Line.of("name", "color")));
		LineService lineService = new LineService(lineRepository, stationService);

		// when
		List<LineResponse> responses = lineService.findLines();

		// then
		assertThat(responses).hasSize(1);
	}
}

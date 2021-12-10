package study.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.test.mock.mockito.*;

import com.google.common.collect.*;

import nextstep.subway.line.application.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

@DisplayName("단위 테스트 - SpringExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
public class SpringExtensionTest {
    @MockBean
    private LineReadService lineReadService;

    @MockBean
    private LineRepository lineRepository;

    @MockBean
    private StationRepository stationRepository;

    @Test
    void findAllLines() {
        // given
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
        LineReadService lineReadService = new LineReadService(lineRepository, stationRepository);

        // when
        List<LineResponse> responses = lineReadService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}

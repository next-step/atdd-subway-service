package study.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.google.common.collect.*;

import nextstep.subway.line.application.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

@DisplayName("단위 테스트 - mockito를 활용한 가짜 협력 객체 사용")
class MockitoTest {
    @Test
    void findAllLines() {
        // given
        LineRepository lineRepository = mock(LineRepository.class);
        StationRepository stationRepository = mock(StationRepository.class);

        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line()));
        when(stationRepository.findAll()).thenReturn(Lists.newArrayList(new Station()));
        LineReadService lineReadService = new LineReadService(lineRepository, stationRepository);

        // when
        List<LineResponse> responses = lineReadService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}

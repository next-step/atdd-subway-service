package study.unit;

import com.google.common.collect.Lists;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("단위 테스트 - mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(MockitoExtension.class)
public class MockitoExtensionTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @Test
    void findAllLines() {
        // given
        Line line = new Line("2호선", "green", new Station("서울대입구역"), new Station("종합운동장"), 9);
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(line));
        LineService lineService = new LineService(lineRepository, stationService);

        // when
        List<LineResponse> responses = lineService.findLines();

        // then
        assertThat(responses).hasSize(1);
    }
}

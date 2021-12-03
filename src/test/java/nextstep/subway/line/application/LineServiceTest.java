package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단위 테스트 - mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StationService stationService;

    @Test
    @DisplayName("노선 생성시 상행종점, 하행종점 같이 생성됨")
    void saveLine() {
        // given
        LineServiceV2 lineService = new LineServiceV2(lineRepository, stationService);
        Station 잠실 = new Station("잠실");
        Station 잠실새내 = new Station("잠실새내");
        LineRequest request = new LineRequest("2호선", "RED", 1L, 2L, 100);
        Line line = new Line(request.getName(), request.getColor(), 잠실, 잠실새내,
            request.getDistance());

        when(lineRepository.save(any())).thenReturn(line);

        // when
        LineResponse response = lineService.saveLine(request);

        // then
        assertThat(response.getStations()).extracting("name").containsExactly("잠실", "잠실새내");
    }

}

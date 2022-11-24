package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("지하철 노선 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @DisplayName("지하철 노선 생성 후 요청한 노선이름, 색상, 지하철역 갯수를 확인한다.")
    @Test
    void saveLine() {
        // given
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        when(stationService.findById(any())).thenReturn(강남역);
        when(stationService.findById(any())).thenReturn(광교역);

        Line line = new Line("신분당선", "red", 강남역, 광교역, 10);
        when(lineRepository.save(any())).thenReturn(line);

        // when
        LineRequest request = new LineRequest("신분당선", "red", 1L, 2L, 10);
        LineResponse response = lineService.saveLine(request);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("신분당선"),
                () -> assertThat(response.getColor()).isEqualTo("red"),
                () -> assertThat(response.getStations()).hasSize(2)
        );
    }
}

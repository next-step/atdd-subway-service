package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private Station 강남역;
    private Station 광교역;
    private Station 죽전역;
    private Station 수원역;
    private Line 신분당선;
    private Line 분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        죽전역 = new Station("죽전역");
        수원역 = new Station("수원역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
        분당선 = new Line("분당선", "yellow", 죽전역, 수원역, 10);
    }

    @DisplayName("지하철 노선 생성 후 요청한 노선이름, 색상, 지하철역 갯수를 확인한다.")
    @Test
    void saveLine() {
        // given
        when(stationService.findById(any())).thenReturn(강남역);
        when(stationService.findById(any())).thenReturn(광교역);
        when(lineRepository.save(any())).thenReturn(신분당선);

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

    @DisplayName("지하철 노선 생성 후 지하철 노선 목록 조회 결과 확인")
    @Test
    void findLines() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 분당선));

        List<LineResponse> lines = lineService.findLines();

        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.get(0).getName()).isEqualTo("신분당선"),
                () -> assertThat(lines.get(1).getName()).isEqualTo("분당선")
        );
    }

    @DisplayName("조회한 지하철 노선의 이름, 색상, 지하철 역 갯수를 확인한다.")
    @Test
    void findLineResponseById() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));

        LineResponse response = lineService.findLineResponseById(any());

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("신분당선"),
                () -> assertThat(response.getColor()).isEqualTo("red"),
                () -> assertThat(response.getStations()).hasSize(2)
        );
    }

    @DisplayName("지하철 노선의 이름과 색상 변경 시 정상적으로 변경됨을 확인한다.")
    @Test
    void updateLine() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));

        lineService.updateLine(1L, new LineRequest("분당선", "yellow", 1L, 2L, 10));

        assertAll(
                () -> assertThat(신분당선.getName()).isEqualTo("분당선"),
                () -> assertThat(신분당선.getColor()).isEqualTo("yellow")
        );
    }
}

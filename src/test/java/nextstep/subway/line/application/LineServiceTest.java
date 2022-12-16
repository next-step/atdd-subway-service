package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("지하철 노선 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private LineService lineService;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private LineRequest 신분당선_요청;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
        신분당선_요청 = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100);
        신분당선 = Line.builder()
                .id(1L)
                .name("신분당선")
                .color("bg-red-600")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .addedFare(100)
                .build();
    }

    @DisplayName("지하철 노선의 상행, 하행역은 반드시 존재해야 한다.")
    @Test
    void 지하철_노선의_상행역은_반드시_존재해야_한다() {
        // given
        when(stationService.findStationById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.saveLine(신분당선_요청));
    }

    @DisplayName("지하철 노선을 저장한다.")
    @Test
    void 지하철_노선을_저장한다() {
        // given
        when(stationService.findStationById(anyLong())).thenReturn(강남역);
        when(stationService.findStationById(anyLong())).thenReturn(양재역);
        when(lineRepository.save(any())).thenReturn(신분당선);

        // when
        LineResponse 저장된_지하철_노선 = lineService.saveLine(신분당선_요청);

        // then
        assertThat(저장된_지하철_노선.getColor()).isEqualTo(신분당선.getColor());
        assertThat(저장된_지하철_노선.getName()).isEqualTo(신분당선.getName());
        assertThat(저장된_지하철_노선.getStations()).hasSize(2);
    }

    @DisplayName("지하철 노선 전체 목록을 조회한다.")
    @Test
    void 지하철_노선_전체_목록을_조회한다() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(new Line(), new Line()));

        // when
        List<LineResponse> 조회된_지하철_노선 = lineService.findLines();

        // then
        assertThat(조회된_지하철_노선).hasSize(2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선을_조회한다() {
        // given
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(신분당선));

        // when
        Line 조회된_지하철_노선 = lineService.findLineById(1L);

        // then
        assertThat(조회된_지하철_노선.getColor()).isEqualTo(신분당선.getColor());
        assertThat(조회된_지하철_노선.getName()).isEqualTo(신분당선.getName());
        assertThat(조회된_지하철_노선.getStations()).hasSize(2);
    }

    @DisplayName("지하철 노선을 조회에 실패한다.")
    @Test
    void 지하철_노선을_조회에_실패한다() {
        // given
        when(lineRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.findLineById(1L));
    }

    @DisplayName("노선명과 노선색상을 수정한다.")
    @Test
    void 노선명과_노선색상을_수정한다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));

        // when
        lineService.updateLine(1L, new LineRequest("신분당선", "bg-orange", null, null, 0, 0));

        // then
        assertAll(() -> {
            assertThat(신분당선.getColor()).isEqualTo("bg-orange");
            assertThat(신분당선.getName()).isEqualTo("신분당선");
        });
    }

    @DisplayName("추가하려는 구간이 존재하지 않습니다.")
    @Test
    void 추가하려는_구간이_존재하지_않습니다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, 3)));
    }

    @DisplayName("이미 등록된 구간 입니다.")
    @Test
    void 이미_등록된_구간_입니다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(양재역);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, 3)));
    }

    @DisplayName("기존에 존재하던 노선의 구간 사이에 새로운 역을 등록한다.")
    @Test
    void 기존에_존재하던_노선의_구간_사이에_새로운_역을_등록한다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(판교역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 2));

        // then
        assertThat(신분당선.getStations()).containsExactly(강남역, 판교역, 양재역);
    }

    @DisplayName("역과 역 사이의 거리보다 좁은 거리를 입력해야 한다.")
    @Test
    void 역과_역_사이의_거리보다_좁은_거리를_입력해야_한다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(판교역);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, 10)));
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다.")
    @Test
    void 새로운_역을_하행_종점으로_등록한다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(판교역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 10));

        // then
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 판교역);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다.")
    @Test
    void 새로운_역을_상행_종점으로_등록한다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(판교역);
        when(stationService.findStationById(3L)).thenReturn(강남역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 10));

        // then
        assertThat(신분당선.getStations()).containsExactly(판교역, 강남역, 양재역);
    }

    @DisplayName("종점이 아닌 역을 제거하면 노선에서 해당 역이 제거된다.")
    @Test
    void 종점이_아닌_역을_제거하면_노선에서_해당_역이_제거된다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(판교역);
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 10));

        // when
        lineService.removeLineStation(1L, 2L);

        // then
        assertThat(신분당선.getStations()).containsExactly(강남역, 판교역);
    }

    @DisplayName("종점을 제거하면 노선에서 해당 역이 제거된다.")
    @Test
    void 종점을_제거하면_노선에서_해당_역이_제거된다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(판교역);
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 10));

        // when
        lineService.removeLineStation(1L, 3L);

        // then
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역);
    }

    @DisplayName("구간이 1개이면 해당역을 제거할 수 없다.")
    @Test
    void 구간이_1개이면_해당역을_제거할_수_없다() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(2L)).thenReturn(양재역);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> lineService.removeLineStation(1L, 2L));
    }
}

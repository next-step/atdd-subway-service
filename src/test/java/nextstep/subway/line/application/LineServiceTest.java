package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    private Line 신분당선;
    private Line 구분당선;
    private Line 현분당선;
    private Station 강남역;
    private Station 광교역;
    private Station 양재역;

    @BeforeEach
    public void setUp() {
        구분당선 = new Line("구분당선", "blue");
        현분당선 = new Line("현분당선", "yellow");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
        신분당선 = new Line("신분당선", "red", 강남역, 광교역, 10);
    }

    @DisplayName("노선을 저장 할 수 있다.")
    @Test
    void line_save() {
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10);
        given(stationService.findById(1L)).willReturn(강남역);
        given(stationService.findById(2L)).willReturn(양재역);
        given(lineRepository.save(any())).willReturn(lineRequest.toLine());

        lineService.saveLine(lineRequest);

        then(lineRepository).should()
                .save(any());

    }

    @DisplayName("노선 목록을 조회할 수 있다.")
    @Test
    void find_lines() {
        given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선, 구분당선));

        List<LineResponse> lines = lineService.findLines();

        assertAll(
                () -> assertThat(lines).isNotEmpty(),
                () -> assertThat(lines.size()).isEqualTo(2),
                () -> assertThat(lines.get(0).getName()).isEqualTo(신분당선.getName()),
                () -> assertThat(lines.get(1).getName()).isEqualTo(구분당선.getName())
        );
    }

    @DisplayName("노선을 조회할 수 있다.")
    @Test
    void find_line() {
        given(lineRepository.findById(1L)).willReturn(Optional.of(신분당선));

        LineResponse lines = lineService.findLineResponseById(1L);

        assertAll(
                () -> assertThat(lines).isNotNull(),
                () -> assertThat(lines.getName()).isEqualTo(신분당선.getName()),
                () -> assertThat(lines.getColor()).isEqualTo(신분당선.getColor())
        );
    }

    @DisplayName("노선의 이름과 색을 변경할 수 있다 ")
    @Test
    void update_line() {
        given(lineRepository.findById(1L)).willReturn(Optional.of(현분당선));

        lineService.updateLine(1L, new LineRequest("바뀐분당선", "pink", null, null, 0));

        assertAll(
                () -> assertThat(현분당선.getName()).isEqualTo("바뀐분당선"),
                () -> assertThat(현분당선.getColor()).isEqualTo("pink")
        );
    }

    @DisplayName("노선에 역을 추가할 수 있다.")
    @Test
    void add_station() {
        given(stationService.findById(1L)).willReturn(강남역);
        given(stationService.findById(2L)).willReturn(양재역);
        given(lineRepository.findById(1L)).willReturn(Optional.of(신분당선));

        lineService.addLineStation(1L, new SectionRequest(1L, 2L, 3));

        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("노선에서 역을 제거할 수 있다.")
    @Test
    void remove_station() {
        given(stationService.findById(1L)).willReturn(강남역);
        given(stationService.findById(3L)).willReturn(양재역);
        given(lineRepository.findById(1L)).willReturn(Optional.of(신분당선));
        lineService.addLineStation(1L, new SectionRequest(1L, 3L, 3));

        lineService.removeLineStation(1L, 3L);

        assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }
}

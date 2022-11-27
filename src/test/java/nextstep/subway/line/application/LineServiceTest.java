package nextstep.subway.line.application;

import nextstep.subway.exception.DuplicatedSectionException;
import nextstep.subway.exception.EmptySectionException;
import nextstep.subway.exception.InvalidSectionDistanceException;
import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    private Station 신사역;
    private Station 강남역;
    private Station 광교역;
    private Station 양재역;
    private Station 죽전역;
    private Station 수원역;
    private Line 신분당선;
    private Line 분당선;

    @BeforeEach
    void setUp() {
        신사역 = new Station("신사역");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        양재역 = new Station("양재역");
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

    @DisplayName("지하철 노선에 이미 등록된 지하철 구간을 등록하면 예외가 발생한다.")
    @Test
    void addLineStationWithException1() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(광교역);

        Assertions.assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 2L, 10)))
                .isInstanceOf(DuplicatedSectionException.class)
                .hasMessageStartingWith(ExceptionMessage.DUPLICATED_SECTION);
    }

    @DisplayName("지하철 노선에 존재하지 않는 상행역, 하행역으로 지하철 구간 추가 요청 시 예외가 발생한다.")
    @Test
    void addLineStationWithException2() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(3L)).thenReturn(죽전역);
        when(stationService.findStationById(4L)).thenReturn(수원역);

        Assertions.assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(3L, 4L, 10)))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION);
    }

    @DisplayName("지하철 노선에 상행역-신규역 구간을 추가할 때 기존 지하철 구간(상행역-하행역) 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {10, 20, 30})
    void addLineStationWithException3(int input) {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(양재역);

        Assertions.assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 2L, input)))
                .isInstanceOf(InvalidSectionDistanceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION_DISTANCE);
    }

    @DisplayName("지하철 노선에 신규역-하행역 구간을 추가할 때 기존 지하철 구간(상행역-하행역) 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {10, 20, 30})
    void addLineStationWithException4(int input) {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(양재역);
        when(stationService.findStationById(2L)).thenReturn(광교역);

        Assertions.assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(1L, 2L, input)))
                .isInstanceOf(InvalidSectionDistanceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION_DISTANCE);
    }

    @DisplayName("지하철 구간이 없는 지하철 노선에 지하철 구간을 등록한다.")
    @Test
    void addLineStation1() {
        Line line = new Line("신분당선", "red");
        when(lineRepository.findById(any())).thenReturn(Optional.of(line));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(광교역);

        lineService.addLineStation(1L, new SectionRequest(1L, 2L, 10));

        Assertions.assertThat(line.getStations()).containsExactly(강남역, 광교역);
    }

    @DisplayName("지하철 노선에 지하철 구간을 등록한다.")
    @Test
    void addLineStation2() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(3L)).thenReturn(양재역);

        lineService.addLineStation(1L, new SectionRequest(1L, 3L, 5));

        Assertions.assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 광교역);
    }

    @DisplayName("지하철 노선에 새로운 상행역으로 지하철 구간을 등록한다.")
    @Test
    void addLineStation3() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(신사역);
        when(stationService.findStationById(2L)).thenReturn(강남역);

        lineService.addLineStation(1L, new SectionRequest(1L, 2L, 5));

        Assertions.assertThat(신분당선.getStations()).containsExactly(신사역, 강남역, 광교역);
    }

    @DisplayName("지하철 노선에 새로운 하행역으로 지하철 구간을 등록한다.")
    @Test
    void addLineStations4() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(1L)).thenReturn(광교역);
        when(stationService.findStationById(2L)).thenReturn(수원역);

        lineService.addLineStation(1L, new SectionRequest(1L, 2L, 5));

        Assertions.assertThat(신분당선.getStations()).containsExactly(강남역, 광교역, 수원역);
    }

    @DisplayName("지하철 구간이 1개인 경우 지하철역 제거 시 예외를 발생한다.")
    @Test
    void removeLineStationException() {
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(any())).thenReturn(강남역);

        Assertions.assertThatThrownBy(() -> lineService.removeLineStation(1L, 1L))
                .isInstanceOf(EmptySectionException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_SECTION);
    }

    @DisplayName("지하철 노선에서 상행 종점역 제거하기")
    @Test
    void removeLineStation1() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 5));
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(any())).thenReturn(강남역);

        lineService.removeLineStation(1L, 1L);

        Assertions.assertThat(신분당선.getStations()).containsExactly(양재역, 광교역);
    }

    @DisplayName("지하철 노선에서 하행 종점역 제거하기")
    @Test
    void removeLineStation2() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 5));
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(any())).thenReturn(광교역);

        lineService.removeLineStation(1L, 1L);

        Assertions.assertThat(신분당선.getStations()).containsExactly(강남역, 양재역);
    }

    @DisplayName("지하철 노선에서 중간역 제거하기")
    @Test
    void removeLineStation3() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 5));
        when(lineRepository.findById(any())).thenReturn(Optional.of(신분당선));
        when(stationService.findStationById(any())).thenReturn(양재역);

        lineService.removeLineStation(1L, 1L);

        Assertions.assertThat(신분당선.getStations()).containsExactly(강남역, 광교역);
    }
}

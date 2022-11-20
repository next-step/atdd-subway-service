package nextstep.subway.line.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Name;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 노선 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 삼성역;
    private Station 종합운동장역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation("교대역");
        강남역 = createStation("강남역");
        역삼역 = createStation("역삼역");
        선릉역 = createStation("선릉역");
        삼성역 = createStation("삼성역");
        종합운동장역 = createStation("종합운동장역");
        이호선 = createLine("2호선", "bg-green", 강남역, 삼성역, 10);
    }

    @DisplayName("노선을 생성한다.")
    @Test
    void saveLine() {
        // given
        LineRequest lineRequest = new LineRequest(이호선.getName().value(), 이호선.getColor().value(), 1L, 2L, 10);
        Line line = lineRequest.toLine(강남역, 삼성역);
        when(stationService.findById(1L))
                .thenReturn(강남역);
        when(stationService.findById(2L))
                .thenReturn(삼성역);
        when(lineRepository.save(line))
                .thenReturn(이호선);

        // when
        LineResponse response = lineService.saveLine(lineRequest);

        // then
        assertAll(
                () -> assertThat(response.getColor()).isEqualTo(이호선.getColor().value()),
                () -> assertThat(response.getName()).isEqualTo(이호선.getName().value()),
                () -> assertThat(response.getStations()).hasSize(2)
        );
    }

    @DisplayName("노선을 조회한다.")
    @Test
    void findLineById() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        // when
        LineResponse response = lineService.findLineResponseById(1L);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(이호선.getName().value()),
                () -> assertThat(response.getColor()).isEqualTo(이호선.getColor().value()),
                () -> assertThat(response.getStations()).hasSize(2),
                () -> assertThat(response.getStations().stream()
                        .map(StationResponse::getName)).containsExactly(강남역.getName().value(), 삼성역.getName().value())
        );
    }

    @DisplayName("노선을 조회할 때 존재하는 노선이 없으면 에러가 발생한다.")
    @Test
    void findLineByIdThrowErrorIfNotExists() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lineService.findLineResponseById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.해당하는_노선_없음.getErrorMessage());
    }

    @DisplayName("노선 전체 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        Station upStation2 = createStation("양재역");
        Station downStation2 = createStation("매봉역");
        Line line2 = new Line("3호선", "bg-orange", upStation2, downStation2, 25);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, line2));

        // when
        List<LineResponse> lines = lineService.findLines();

        // then
        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.stream().map(LineResponse::getColor)).contains(이호선.getColor().value(), "bg-orange"),
                () -> assertThat(lines.stream().map(LineResponse::getName)).contains(이호선.getName().value(), "3호선")
        );
    }

    @DisplayName("노선명과 노선색상을 수정한다.")
    @Test
    void updateLineColorAndLineName() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        // when
        lineService.updateLine(1L, new LineRequest("3호선", "bg-orange", null, null, 0));

        // then
        assertAll(
                () -> assertThat(이호선.getColor()).isEqualTo(Color.from("bg-orange")),
                () -> assertThat(이호선.getName()).isEqualTo(Name.from("3호선"))
        );
    }

    @DisplayName("노선명과 노선색상 수정 시, 노선명이 비어있으면 노선색상만 수정한다.")
    @Test
    void updateLineColor() {
        // given
        Name actualName = 이호선.getName();
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        // when
        lineService.updateLine(1L, new LineRequest(null, "bg-orange", null, null, 0));

        // then
        assertAll(
                () -> assertThat(이호선.getColor()).isEqualTo(Color.from("bg-orange")),
                () -> assertThat(이호선.getName()).isEqualTo(actualName)
        );
    }

    @DisplayName("노선명과 노선색상 수정 시, 노선색상이 비어있으면 노선명만 수정한다.")
    @Test
    void updateLineName() {
        // given
        Color actualColor = 이호선.getColor();
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        // when
        lineService.updateLine(1L, new LineRequest("3호선", null, null, null, 0));

        // then
        assertAll(
                () -> assertThat(이호선.getColor()).isEqualTo(actualColor),
                () -> assertThat(이호선.getName()).isEqualTo(Name.from("3호선"))
        );
    }

    @DisplayName("기존에 존재하던 노선의 구간 사이에 새로운 역을 등록한다.")
    @Test
    void addStationBetweenLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(강남역);
        when(stationService.findById(3L)).thenReturn(역삼역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 3));

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다.")
    @Test
    void addStationInFrontOfUpStation() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(교대역);
        when(stationService.findById(3L)).thenReturn(강남역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 3));

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(교대역, 강남역, 삼성역);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다.")
    @Test
    void addStationAfterDownStation() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(삼성역);
        when(stationService.findById(3L)).thenReturn(종합운동장역);

        // when
        lineService.addLineStation(1L, new SectionRequest(2L, 3L, 3));

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(강남역, 삼성역, 종합운동장역);
    }

    @DisplayName("기존 역 사이 거리보다 크거나 같은 거리의 역을 노선에 등록한다.")
    @ParameterizedTest(name = "기존 역 사이 거리(10)보다 {0}은 크거나 같으므로 구간이 생성되지 않는다.")
    @ValueSource(ints = {10, 12, 25})
    void addSectionWhichHasEqualOrLongerDistance(int currentDistance) {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(역삼역);
        when(stationService.findById(3L)).thenReturn(삼성역);

        // when & then
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, currentDistance)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
    }

    @DisplayName("노선에 기등록된 역들의 구간을 등록하면 새로운 구간이 생성되지 않는다.")
    @Test
    void addSectionDuplicateInLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(강남역);
        when(stationService.findById(3L)).thenReturn(삼성역);

        // when & then
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이미_존재하는_구간.getErrorMessage());
    }

    @DisplayName("노선에 등록되지 않은 역들의 구간을 등록하면 새로운 구간이 생성되지 않는다.")
    @Test
    void addSectionNotInLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(역삼역);
        when(stationService.findById(3L)).thenReturn(선릉역);

        // when & then
        assertThatThrownBy(() -> lineService.addLineStation(1L, new SectionRequest(2L, 3L, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.구간의_상행역과_하행역이_모두_노선에_포함되지_않음.getErrorMessage());
    }

    @DisplayName("노선 내 상행/하행 종점이 아닌 역을 제거하면 노선에서 해당 역이 제거된다.")
    @Test
    void deleteStationInMiddle() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(역삼역);
        이호선.addSection(createSection(이호선, 강남역, 역삼역, 2));

        // when
        lineService.removeLineStation(1L, 2L);

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("노선의 상행 종점을 제거하면 노선에서 해당 역이 제거되고 상행 종점이 바뀐다.")
    @Test
    void deleteStationWhichIsUpStation() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(강남역);
        이호선.addSection(createSection(이호선, 강남역, 역삼역, 2));

        // when
        lineService.removeLineStation(1L, 2L);

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(역삼역, 삼성역);
    }

    @DisplayName("노선의 하행 종점을 제거하면 노선에서 해당 역이 제거되고 하행 종점이 바뀐다.")
    @Test
    void deleteStationWhichIsDownStation() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(삼성역);
        이호선.addSection(createSection(이호선, 강남역, 역삼역, 2));

        // when
        lineService.removeLineStation(1L, 2L);

        // then
        assertThat(이호선.findInOrderStations()).containsExactly(강남역, 역삼역);
    }

    @DisplayName("노선에 구간이 1개이면 해당 구간에 포함된 역을 노선에서 제거할 수 없다.")
    @Test
    void deleteStationWhenLineHasOneSection() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(stationService.findById(2L)).thenReturn(강남역);

        // when & then
        assertThatThrownBy(() -> lineService.removeLineStation(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선에_속한_구간이_하나이면_제거_불가.getErrorMessage());
    }

//    @DisplayName("노선에 등록되지 않은 역을 제거하면 노선에서 역이 제거되지 않는다.")
//    @Test
//    void deleteStationNotInLine() {
//        // given
//        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
//        when(stationService.findById(2L)).thenReturn(종합운동장역);
//
//        // when & then
//        assertThatThrownBy(() -> lineService.removeLineStation(1L, 2L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(ErrorCode.노선_내_존재하지_않는_역.getErrorMessage());
//    }
}

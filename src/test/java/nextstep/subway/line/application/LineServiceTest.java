package nextstep.subway.line.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
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
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = createStation("강남역");
        역삼역 = createStation("역삼역");
        이호선 = createLine("2호선", "bg-green", 강남역, 역삼역, 10);
    }

    @DisplayName("노선을 생성한다.")
    @Test
    void saveLine() {
        // given
        LineRequest lineRequest = new LineRequest(이호선.getName().value(), 이호선.getColor().value(), 1L, 2L, 10);
        Line line = lineRequest.toLine(강남역, 역삼역);
        when(stationService.findById(1L))
                .thenReturn(강남역);
        when(stationService.findById(2L))
                .thenReturn(역삼역);
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
                        .map(StationResponse::getName)).containsExactly(강남역.getName().value(), 역삼역.getName().value())
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
}

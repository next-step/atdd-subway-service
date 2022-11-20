package nextstep.subway.line.application;

import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
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

    @DisplayName("노선을 생성한다.")
    @Test
    void saveLine() {
        // given
        Station upStation = createStation("강남역");
        Station downStation = createStation("역삼역");
        LineRequest lineRequest = new LineRequest("2호선", "bg-green", 1L, 2L, 10);
        Line line = lineRequest.toLine(upStation, downStation);
        when(stationService.findById(1L))
                .thenReturn(upStation);
        when(stationService.findById(2L))
                .thenReturn(downStation);
        when(lineRepository.save(line))
                .thenReturn(line);

        // when
        LineResponse response = lineService.saveLine(lineRequest);

        // then
        assertAll(
                () -> assertThat(response.getColor()).isEqualTo("bg-green"),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStations()).hasSize(2)
        );
    }

    @DisplayName("노선을 조회한다.")
    @Test
    void findLineById() {
        // given
        Station upStation = createStation("강남역");
        Station downStation = createStation("역삼역");
        Line line = new Line("2호선", "bg-green", upStation, downStation, 10);
        when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

        // when
        LineResponse response = lineService.findLineResponseById(1L);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getColor()).isEqualTo("bg-green"),
                () -> assertThat(response.getStations()).hasSize(2),
                () -> assertThat(response.getStations().stream()
                        .map(StationResponse::getName)).containsExactly("강남역", "역삼역")
        );
    }

    @DisplayName("노선을 조회할 때 존재하는 노선이 없으면 에러가 발생한다.")
    @Test
    void findLineByIdThrowErrorIfNotExists() {
        // given
        Station upStation = createStation("강남역");
        Station downStation = createStation("역삼역");
        Line line = new Line("2호선", "bg-green", upStation, downStation, 10);
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
        Station upStation = createStation("강남역");
        Station downStation = createStation("역삼역");
        Line line = new Line("2호선", "bg-green", upStation, downStation, 10);
        Station upStation2 = createStation("양재역");
        Station downStation2 = createStation("매봉역");
        Line line2 = new Line("3호선", "bg-orange", upStation2, downStation2, 25);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line, line2));

        // when
        List<LineResponse> lines = lineService.findLines();

        // then
        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.stream().map(LineResponse::getColor)).contains("bg-green", "bg-orange"),
                () -> assertThat(lines.stream().map(LineResponse::getName)).contains("2호선", "3호선")
        );
    }
}

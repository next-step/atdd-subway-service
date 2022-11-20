package nextstep.subway.line.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}

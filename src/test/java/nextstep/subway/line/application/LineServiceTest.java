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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private LineService lineService;

    @BeforeEach
    void beforeEach() {
        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("`Line` 생성 후, 등록된 `Station` 목록 반환")
    @Test
    void createLine() {
        // Given
        Station 삼성역 = new Station("삼성역");
        Station 잠실역 = new Station("잠실역");
        Line _2호선 = new Line("2호선", "GREEN", 삼성역, 잠실역, 1150);
        when(lineRepository.save(any())).thenReturn(_2호선);
        // When
        LineResponse lineResponse = lineService.saveLine(new LineRequest());
        // Then
        assertThat(lineResponse.getStations())
                .extracting("name")
                .containsExactly("삼성역", "잠실역");
    }
}

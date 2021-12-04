package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("주어진 역이 포함된 모든 라인을 가져온다")
    @Test
    void testFindLineByStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 남부터미널 = new Station("남부터미널");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Line 신분당선 = new Line("신분당선", "green", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "green", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널, 3);

        Mockito.when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        List<Line> lines = lineService.findLineByStation(강남역);

        // then
        Mockito.verify(lineRepository).findAll();
        assertThat(lines).hasSize(2)
                .map(Line::getName)
                .containsOnly("이호선", "신분당선");
    }
}

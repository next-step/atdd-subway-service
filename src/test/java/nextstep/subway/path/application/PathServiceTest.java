package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private PathService pathService;

    private List<Line> lines;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역 --- *3호선* --- 양재역
     */
    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

        final Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10, 300);
        final Line 이호선 = new Line("2호선", "green", 교대역, 강남역, 10, 0);
        final Line 삼호선 = new Line("3호선", "orange", 교대역, 양재역, 5, 500);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        lines = Arrays.asList(신분당선, 이호선, 삼호선);
    }

    @ParameterizedTest
    @CsvSource(value = {"5:0", "12:750", "18:1200", "19:1850"}, delimiter = ':')
    void computeShortestDistance(int memberAge, int expectedFare) {
        // given
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findById(anyLong())).thenReturn(남부터미널역).thenReturn(강남역);

        // when
        final PathResponse pathResponse = pathService.computeShortestDistance(1L, 2L, memberAge);

        // then
        assertThat(pathResponse.getStations())
            .containsExactly(StationResponse.of(남부터미널역), StationResponse.of(양재역), StationResponse.of(강남역));
        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }
}

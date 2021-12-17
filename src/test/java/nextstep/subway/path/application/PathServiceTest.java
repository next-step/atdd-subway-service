package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;
    
    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;
    

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    
    @BeforeEach
    void setUp() {
        교대역 = Station.from("교대역");
        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        남부터미널역 = Station.from("남부터미널역");
        
        이호선 = Line.of("이호선", "bg-green-600", 교대역, 강남역, Distance.from(30), 300);
        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, Distance.from(50), 900);
        삼호선 = Line.of("삼호선", "bg-orange-600", 교대역, 남부터미널역, Distance.from(60), 500);

        삼호선.addSection(Section.of(삼호선, 남부터미널역, 양재역, Distance.from(30)));
    }
    
    @DisplayName("지하철 최단 경로를 조회한다")
    @Test
    void 최단_경로_조회() {
        // given
        when(stationService.findById(any())).thenReturn(교대역, 양재역);
        when(lineService.findLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));

        // when
        PathResponse response = pathService.findShortestPath(1L, 2L, 30);
        List<Station> stations = response.getStations()
                .stream()
                .map(stationResponse -> Station.from(stationResponse.getName()))
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(stations).containsExactlyElementsOf(Arrays.asList(교대역, 강남역, 양재역)),
                () -> assertThat(response.getDistance()).isEqualTo(80),
                () -> assertThat(response.getFare()).isEqualTo(3350)    // 거리요금 2450 + 노선 추가요금 900
                );
    }

}

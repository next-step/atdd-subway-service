package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private PathService pathService;

    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 교대역 = new Station("교대역");
    private Station 남부터미널역 = new Station("남부터미널역");

    private Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, new Distance(10));
    private Line 이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, new Distance(10));
    private Line 삼호선 = new Line("삼호선", "bg-yellow-600", 교대역, 양재역, new Distance(5));

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationService);
        pathService = new PathService(stationService, lineService);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));
    }

    @Test
    void findShortestRoute() {
        //given
        when(stationService.findStationById(1L)).thenReturn(교대역);
        when(stationService.findStationById(5L)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        //when
        PathResponse pathResponse = pathService.findShortestRoute(1L, 5L,
                new LoginMember(1L, "test@test.com", 40));

        //then
        assertThat(pathResponse.getStations().stream().map(station -> station.getName()).collect(Collectors.toList()))
                .containsExactly("교대역","남부터미널역","양재역");
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFee()).isEqualTo(1250);
    }
}

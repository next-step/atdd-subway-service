package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class PathServiceTest {

    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    Station 강남역;
    Station 교대역;
    Station 남부터미널역;
    Station 양재역;
    @BeforeEach
    public void init(){
        강남역 = new Station("강남역");

        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        이호선 = new Line("이호선", "녹색", 강남역, 교대역, 10);
        신분당선 = new Line("신분당선", "빨강색", 강남역, 양재역, 9);
        삼호선 = new Line("삼호선", "주황색", 남부터미널역, 양재역, 8);
        삼호선.addStation(교대역, 남부터미널역, 7);

    }
    /**
     * 교대역    --- *2호선*(10) ---   강남역
     * |                        |
     * *3호선*(7)                   *신분당선*(9)
     * |                        |
     * 남부터미널역  --- *3호선*(8) ---   양재
     */
    @Test
    public void 정상_경로찾기(){
        //given
        when(lineService.findAllLines()).thenReturn(Arrays.asList(이호선, 신분당선, 삼호선));
        when(stationService.findById(1L)).thenReturn(교대역);
        when(stationService.findById(3L)).thenReturn(양재역);
        PathService pathService = new PathService(lineService, stationService);

        //when
        PathResponse pathResponse = pathService.findPath(1L, 3L);

        //then
        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(15),
            () -> assertThat(pathResponse.getStations()).extracting("name")
                .containsExactly("교대역","남부터미널역","양재역")
        );
    }

}

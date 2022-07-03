package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
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

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 군자역;
    private Station 미사역;
    private Station 왕십리역;

    /**
     * 교대역    --- *2호선* (7) ---   강남역
     * |                              |
     * *3호선*                      *신분당선*
     * (3)                           (10)
     * |                              |
     * 남부터미널역  --- *3호선* (2)---   양재
     *
     * 왕십리역 --(15)-- 군자역    --- *5호선* (55) ---   미사역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        왕십리역 = new Station("왕십리역");
        군자역 = new Station("군자역");
        미사역 = new Station("미사역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 200);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 7, 100);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5, 0);
        오호선 = new Line("오호선", "bg-purple-600",군자역, 미사역, 55, 500);

        오호선.addSection(왕십리역, 군자역, 15);
        삼호선.addSection(교대역, 남부터미널역, 3);
        pathService = new PathService(lineRepository, stationService);
    }

    @DisplayName("양재역과 교대역의 최단 경로를 조회한다.")
    @Test
    void 최단_경로_조회() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(교대역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(2L, 3L, 20);

        //then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getFare()).isEqualTo(1250);

    }

    @Test
    void 추가요금이_있는_노선을_환승하여_이용할_경우_요금_비교_기본요금() {
        //강남역 - 교대역 - 남부터미널
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(4L)).thenReturn(남부터미널역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(1L, 4L, 20);

        //then 기본 요금 1250원 + 이호선의 추가요금 100원
        assertThat(pathResponse.getFare()).isEqualTo(1350);
    }

    @Test
    void 추가요금이_있는_노선을_이용할_경우_요금_비교_50km_초과() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(5L)).thenReturn(군자역);
        when(stationService.findStationById(6L)).thenReturn(미사역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(5L, 6L, 20);

        //then 기본요금 1250 + 거리별 추가요금 700 + 노선 이용추가요금 500
        assertThat(pathResponse.getFare()).isEqualTo(2450);
    }

    @Test
    void 청소년_고객의_요금_비교_기본요금() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(교대역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(2L, 3L, 18);

        //then 기본요금 1250 - ((1250 - 350) * 0.2) = 1070
        assertThat(pathResponse.getFare()).isEqualTo(1070);
    }

    @Test
    void 청소년_고객이_추가요금이_있는_노선을_이용할_경우_요금_비교_10km_초과() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(5L)).thenReturn(군자역);
        when(stationService.findStationById(7L)).thenReturn(왕십리역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(5L, 7L, 18);

        //then 기본요금 1250 + 거리별 추가요금 300 + 노선 이용추가요금 500 - ((2050 - 350) * 0.2)
        assertThat(pathResponse.getFare()).isEqualTo(1710);
    }

    @Test
    void 청소년_고객이_추가요금이_있는_노선을_이용할_경우_요금_비교_50km_초과() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(5L)).thenReturn(군자역);
        when(stationService.findStationById(6L)).thenReturn(미사역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(5L, 6L, 18);

        //then 기본요금 1250 + 거리별 추가요금 700 + 노선 이용추가요금 500 - ((2450 - 350) * 0.2)
        assertThat(pathResponse.getFare()).isEqualTo(2030);
    }

    @Test
    void 어린이_고객의_요금_비교_기본요금() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(교대역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(2L, 3L, 6);

        //then 기본요금 1250 - ((1250 - 350) * 0.2) = 800
        assertThat(pathResponse.getFare()).isEqualTo(800);
    }

    @Test
    void 어린이_고객이_추가요금이_있는_노선을_이용할_경우_요금_비교_10km_초과() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(5L)).thenReturn(군자역);
        when(stationService.findStationById(7L)).thenReturn(왕십리역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(5L, 7L, 6);

        //then 기본요금 1250 + 거리별 추가요금 300 + 노선 이용추가요금 500 - ((2050 - 350) * 0.5)
        assertThat(pathResponse.getFare()).isEqualTo(1200);
    }

    @Test
    void 어린이_고객이_추가요금이_있는_노선을_이용할_경우_요금_비교_50km_초과() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(5L)).thenReturn(군자역);
        when(stationService.findStationById(6L)).thenReturn(미사역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(5L, 6L, 6);

        //then 기본요금 1250 + 거리별 추가요금 700 + 노선 이용추가요금 500 - ((2450 - 350) * 0.5)
        assertThat(pathResponse.getFare()).isEqualTo(1400);
    }

    @Test
    void 출발역과_도착역이_같은_경우_에러() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);

        assertThrows(IllegalArgumentException.class, () -> pathService.findShortestPath(2L, 2L, 20));
    }

    @Test
    void 노선에_등록되지_않은_역_최단_경로_조회시_에러_발생() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);

        assertThrows(NoSuchElementFoundException.class,() -> pathService.findShortestPath(2L, 9L, 20));
    }

    @Test
    void 출발역과_도착역이_연결되어_있지_않을_경우_에러_발생() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(6L)).thenReturn(미사역);

        assertThrows(IllegalArgumentException.class, () -> pathService.findShortestPath(2L, 6L, 20));
    }
}

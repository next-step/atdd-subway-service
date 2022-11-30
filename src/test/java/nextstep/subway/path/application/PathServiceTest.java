package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 수인분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 매봉역;
    private Station 양재시민의숲;
    private Station 정자역;

    /**
     * 교대역    --- *2호선* ---   강남역  --- *2호선* --- 역삼역   --- *2호선* ---  선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* ---  매봉
     *                          |
     *                          *신분당선*
     *                          |
     *                          양재시민의숲
     *
     * --- *수인분당선* ---  정자
     */

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        교대역 = 지하철역_생성("교대역");
        남부터미널역 = 지하철역_생성("남부터미널역");
        역삼역 = 지하철역_생성("역삼역");
        선릉역 = 지하철역_생성("선릉역");
        매봉역 = 지하철역_생성("매봉역");
        양재시민의숲 = 지하철역_생성("양재시민의숲");
        정자역 = 지하철역_생성("정자역");

        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재시민의숲, 20);
        이호선 = 지하철_노선_생성("이호선", "bg-green-600", 교대역, 선릉역, 30);
        삼호선 = 지하철_노선_생성("삼호선", "bg-orange-600", 교대역, 매봉역, 30);
        수인분당선 = 지하철_노선_생성("수인분당선", "bg-yello-600", 교대역, 매봉역, 30);
    }


    @Test
    @DisplayName("출발역ID와 도착역ID를 받아 지하철 최단 경로 조회한다.")
    void findPath() {
        // given
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(매봉역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선, 수인분당선));

        // when
        PathResponse pathResponse = pathService.findPath(source, target);

        // then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(30);
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }
}
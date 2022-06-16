package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.NonLoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.policy.TeenagerDiscountPolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    /**
     * 교대역      --- *2호선* ---   강남역
     * |                            |
     * *3호선*                    *신분당선*
     * |                            |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void before() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L,"교대역");
        남부터미널역 = new Station(4L,"남부터미널역");

        신분당선 = new Line("신분당선","red", 강남역, 양재역, 10, 900);
        이호선 = new Line("이호선","green", 교대역, 강남역, 10, 500);
        삼호선 = new Line("삼호선","orange", 교대역, 양재역, 5, 500);

        Section 삼호선_새로운구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
        삼호선.addSection(삼호선_새로운구간);
    }

    @Test
    @DisplayName("최단거리 조회 서비스 테스트")
    void findShortestPath() {
        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(교대역);
        when(lineRepository.findAll()).thenReturn(lines);

        PathResponse pathResponse = pathService.findShortestPath(new NonLoginMember(), 양재역.getId(), 교대역.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(5);
        assertThat(pathResponse.getStations()).
                containsExactlyElementsOf(
                        Arrays.asList(
                                StationResponse.of(양재역),
                                StationResponse.of(남부터미널역),
                                StationResponse.of(교대역)
                        ));
    }
}

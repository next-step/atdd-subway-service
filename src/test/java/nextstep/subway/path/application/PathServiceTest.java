package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = new Line("2호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "bg-orange-600", 교대역, 양재역, 5, 500);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        pathService = new PathService(stationService, lineRepository);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @Test
    @DisplayName("최단 경로 조회")
    void findPath() {
        // given
        Long sourceId = 강남역.getId();
        Long targetId = 남부터미널역.getId();

        // when (mocking)
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findById(sourceId)).thenReturn(강남역);
        when(stationService.findById(targetId)).thenReturn(남부터미널역);

        LoginMember loginMember = new LoginMember(1L, "ehdgml3206@gmail.com", 31);
        PathResponse pathResponse = pathService.findPath(loginMember, new PathRequest(sourceId, targetId));

        // then
        assertAll(
                () -> assertThat(pathResponse).isNotNull(),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12),
                () -> assertThat(pathResponse.getStations())
                        .extracting("name")
                        .containsExactlyElementsOf(Arrays.asList(강남역.getName(), 양재역.getName(), 남부터미널역.getName())),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2250)
        );
    }
}


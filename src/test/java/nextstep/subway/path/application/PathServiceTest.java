package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private Station 강남;
    private Station 교대;
    private Station 남부터미널;
    private Station 양재;

    @Mock
    private StationService stationService;

    @Mock
    private LineRepository lineRepository;

    private PathService pathService;

    /**
     * 교대역 ------- (2) ---- 강남역
     * |                      |
     * (1)                   (4)
     * |                      |
     * 남부터미널역 --- (2) ---- 양재
     */
    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationService);

        강남 = new Station(1L, "강남");
        교대 = new Station(2L, "교대");
        남부터미널 = new Station(3L, "남부터미널");
        양재 = new Station(4L, "양재");

        Line 이호선 = new Line("이호선", "bg-green-600", 0, 교대, 강남, 2);
        Line 삼호선 = new Line("삼호선", "bg-orange-600", 0, 교대, 남부터미널, 1);
        삼호선.addSection(new Section(남부터미널, 양재, 2));
        Line 신분당선 = new Line("신분당선", "bg-red-600", 0, 강남, 양재, 4);

        when(lineRepository.findAll()).thenReturn(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findPath() {
        // given
        when(stationService.findById(교대.getId())).thenReturn(교대);
        when(stationService.findById(양재.getId())).thenReturn(양재);

        // when
        PathResponse path = pathService.findPath(new LoginMember(), 교대.getId(), 양재.getId());

        // then
        assertThat(path.getDistance()).isEqualTo(3);
        assertThat(path.getStations()).extracting("name")
                .isEqualTo(Arrays.asList("교대", "남부터미널", "양재"));
    }
}

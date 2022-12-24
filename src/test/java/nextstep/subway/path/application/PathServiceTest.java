package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("지하철 최단 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    /**
     * 서울역-------1호선(30)----동묘앞
     *  /                        /
     *  /                        /
     * 4호선(10)              6호선(10)
     *  /                       /
     *  /                       /
     * 삼각지----6호선(15)----신당역
     *
     * 옥수역----3호선(5)------교대역
     */
    private Line 일호선;
    private Line 사호선;
    private Line 육호선;
    private Line 삼호선;

    private Station 서울역;
    private Station 동묘앞;
    private Station 신당역;
    private Station 삼각지;
    private Station 옥수역;
    private Station 교대역;

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        서울역 = new Station("서울역");
        동묘앞 = new Station("동묘앞");
        신당역 = new Station("신당역");
        삼각지 = new Station("삼각지");
        옥수역 = new Station("옥수역");
        교대역 = new Station("교대역");

        일호선 = new Line("일호선", "blue", 서울역, 동묘앞, 30);
        사호선 = new Line("사호선", "sky-blue", 서울역, 삼각지, 10);
        삼호선 = new Line("삼호선", "orange", 옥수역, 교대역, 5);
        육호선 = new Line("육호선", "brown", 동묘앞, 신당역, 10);

        육호선.addSection(new Section(육호선, 신당역, 삼각지, 15));

        sections = new ArrayList<>();
        sections.addAll(일호선.getSections());
        sections.addAll(사호선.getSections());
        sections.addAll(육호선.getSections());
        sections.addAll(삼호선.getSections());
    }

    @DisplayName("최단 경로 찾기 - 구간: 서울역- 신당역")
    @Test
    void findTheShortestPath() {
        PathFinder pathFinder = ShortestPathFinder.from(new Sections(sections));

        List<Station> stations = pathFinder.findAllStationsInTheShortestPath(서울역, 신당역);
        int distance = pathFinder.findTheShortestPathDistance(서울역, 신당역);

        assertAll(() -> {
            assertThat(stations).hasSize(3);
            assertThat(distance).isEqualTo(25);
        });
    }

    @DisplayName("예외발생 - 출발역과 도착역이 같은 경우")
    @Test
    void makeExceptionWhenSourceStationEqualsTargetStation() {
        PathFinder pathFinder = ShortestPathFinder.from(new Sections(sections));

        assertThatThrownBy(() -> pathFinder.findAllStationsInTheShortestPath(서울역, 서울역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외발생 - 출발역과 도착역이 연결되지 않은 경우")
    @Test
    void makeExceptionWhenSourceStationIsNotConnectToTargetStation() {
        PathFinder pathFinder = ShortestPathFinder.from(new Sections(sections));

        assertThatThrownBy(() -> pathFinder.findAllStationsInTheShortestPath(서울역, 교대역))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

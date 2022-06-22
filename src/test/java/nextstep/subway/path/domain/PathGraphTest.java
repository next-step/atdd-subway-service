package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PathGraphTest {

    Line 이호선;
    Line 삼호선;
    Line 신분당선;
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;

    Station 없는역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        없는역 = new Station("없는역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
    }

    @Test
    @DisplayName("최단_구간의_경로를_구한다")
    void shortestPath() {
        //given
        List<Section> sections = 모든_노선의_구간을_구한다(Arrays.asList(신분당선, 이호선, 삼호선));
        ShortestPathFinder shortestPathFinder = new JgraphShortestPathFinder(sections);
        PathGraph pathGraph = new PathGraph(shortestPathFinder);

        //when
        Path shortestPath = pathGraph.findShortestPath(교대역, 양재역);

        //then
        assertAll(
                () -> assertThat(shortestPath.getStations()).isEqualTo(Stations.of(Arrays.asList(교대역, 남부터미널역, 양재역))),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(Distance.of(5))
        );
    }

    @Test
    @DisplayName("구간의 없는역을 조회한다.")
    void existNotSectionPath() {
        //given
        List<Section> sections = 모든_노선의_구간을_구한다(Arrays.asList(신분당선, 이호선, 삼호선));
        ShortestPathFinder shortestPathFinder = new JgraphShortestPathFinder(sections);
        PathGraph pathGraph = new PathGraph(shortestPathFinder);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> pathGraph.findShortestPath(교대역, 없는역)
        );
    }

    @Test
    @DisplayName("같은 시작역과 끝역의 경로는 구할수 없다.")
    void duplicationFindStationSectionPath() {
        //given
        List<Section> sections = 모든_노선의_구간을_구한다(Arrays.asList(신분당선, 이호선, 삼호선));
        ShortestPathFinder shortestPathFinder = new JgraphShortestPathFinder(sections);
        PathGraph pathGraph = new PathGraph(shortestPathFinder);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> pathGraph.findShortestPath(교대역, 교대역)
        );
    }

    private List<Section> 모든_노선의_구간을_구한다(List<Line> lines) {
        List<Section> mergeSection = new ArrayList<>();
        for (Line line : lines) {
            final Sections sections = line.getSections();
            mergeSection.addAll(sections.getSectionElements());
        }
        return mergeSection;
    }

}

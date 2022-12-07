package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    final Station 강남역 = new Station("강남역");
    final Station 역삼역 = new Station("역삼역");
    final Station 교대역 = new Station("교대역");
    final Station 양재시민의숲역 = new Station("양재시민의숲역");
    final Station 양재역 = new Station("양재역");
    final Station 남부터미널역 = new Station("남부터미널역");
    final Station 인천역 = new Station("인천역");
    final Station 동인천역 = new Station("동인천역");
    final Station 도원역 = new Station("도원역");

    final Line 일호선 = new Line ("일호선", "bg-blue-600");
    final Line 이호선 = new Line ("이호선", "bg-green-600");
    final Line 삼호선 = new Line ("이호선", "bg-yellow-600");
    final Line 신분당선 = new Line("분당선", "bg-red-600");

    WeightedMultigraph<Station, DefaultWeightedEdge> sectionDistanceGraph
            = new WeightedMultigraph(DefaultWeightedEdge.class);


    /**
     * 교대역    --- *2호선* ---   강남역    --- *2호선* ---   역삼역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재    --- *신분당선* ---   양재시민의숲역
     *
     * 인천역    --- *1호선* ---   동인천역     --- *1호선* ---   도원역
     */
    @BeforeEach
    void setUp() {
        일호선.addSection(new Section(일호선, 도원역, 동인천역, new Distance(10)));
        일호선.addSection(new Section(일호선, 동인천역, 인천역, new Distance(10)));

        이호선.addSection(new Section(이호선, 역삼역, 강남역, new Distance(10)));
        이호선.addSection(new Section(이호선, 강남역, 교대역, new Distance(5)));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(5)));
        삼호선.addSection(new Section(이호선, 남부터미널역, 양재역, new Distance(5)));

        신분당선.addSection(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        신분당선.addSection(new Section(신분당선, 양재역, 양재시민의숲역, new Distance(10)));

    }

    @Test
    @DisplayName("최단 거리 경로의 지하철역 순서목록과 거리를 구한다.")
    void findShortestPathStationListTest() {
        //given
        PathFinder pathFinder = new PathFinder(역삼역, 남부터미널역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(역삼역, 강남역, 교대역, 남부터미널역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(20);
    }

    @Test
    @DisplayName("시작지점과 끝지점을 거꾸로 했을 때 거리가 동일하고 역목록은 거꾸로 나오는지 테스트")
    void sourceTargetReverseEqualPathTest() {
        //given
        PathFinder pathFinder1 = new PathFinder(역삼역, 남부터미널역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        PathFinder pathFinder2 = new PathFinder(남부터미널역, 역삼역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder1.getShortestPathStationList())
                .containsExactly(역삼역, 강남역, 교대역, 남부터미널역);
        assertThat(pathFinder2.getShortestPathStationList())
                .containsExactly(남부터미널역, 교대역, 강남역, 역삼역);
        assertThat(pathFinder1.getShortestPathDistance())
                .isEqualTo(pathFinder2.getShortestPathDistance());
    }

    @Test
    @DisplayName("한 구간의 최단 거리와 경로 구하기 테스트")
    void findOneSectionShortestPathTest() {
        //given
        PathFinder pathFinder = new PathFinder(역삼역, 강남역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(역삼역, 강남역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(10);
    }

    @Test
    @DisplayName("한 노선에서의 최단 거리와 경로 구하기 테스트")
    void findOneLineShortestPathTest() {
        //given
        PathFinder pathFinder = new PathFinder(인천역, 도원역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(인천역, 동인천역, 도원역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(20);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void sourceTargetEqualExceptionTest() {
        assertThatThrownBy(() -> new PathFinder(인천역, 인천역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    void disconnectExceptionTest() {
        assertThatThrownBy(() -> new PathFinder(인천역, 강남역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

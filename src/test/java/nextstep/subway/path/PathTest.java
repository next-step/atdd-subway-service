package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.infra.JgraphtPathFinderStrategy;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.infra.PathFinderStrategy;
import nextstep.subway.path.infra.JgraphtPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.infra.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @DisplayName("최단 경로 찾기")
    @Test
    void findShortestPathTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");
        
        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "red", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "red", 교대역, 양재역, 5);

        삼호선.addSection(남부터미널역, 양재역, 3);

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        //when
        PathFinderStrategy pathFinder = new JgraphtPathFinderStrategy();
        ShortestPath path = new ShortestPath(pathFinder);
        List<Station> paths = path.findPaths(lines, 강남역, 남부터미널역);
        Distance distance = path.findPathWeight(lines, 강남역, 남부터미널역);

        //then
        assertThat(distance).isEqualTo(new Distance(12));
        assertThat(paths.stream().map(Station::getName)).containsExactly("강남역", "교대역", "남부터미널역");
    }

    @DisplayName("출발역과 도착역이 같을 때 최단경로 찾기")
    @Test
    void findShortestPathIfCorrectSourceAndTargetTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "red", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "red", 교대역, 양재역, 5);

        삼호선.addSection(남부터미널역, 양재역, 3);

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        //when
        PathFinderStrategy pathFinder = new JgraphtPathFinderStrategy();
        ShortestPath path = new ShortestPath(pathFinder);

        //then
        assertThatThrownBy(() -> path.findPaths(lines, 강남역, 강남역)).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않을 때 최단경로 찾기")
    @Test
    void findShortestPathIfLinkSourceAndTargetTest() {

        //given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 이호선 = new Line("이호선", "red", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "red", 남부터미널역, 양재역, 5);


        List<Line> lines = Arrays.asList(이호선, 삼호선);

        //when
        PathFinderStrategy pathFinder = new JgraphtPathFinderStrategy();
        ShortestPath path = new ShortestPath(pathFinder);

        assertThatThrownBy(() -> path.findPaths(lines, 강남역, 남부터미널역)).isInstanceOf(IllegalStateException.class);
    }

}
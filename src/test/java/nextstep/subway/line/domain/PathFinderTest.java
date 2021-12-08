package nextstep.subway.line.domain;

import nextstep.subway.exception.IsEqualsTwoStationsException;
import nextstep.subway.exception.NotEnrollStationInGraphException;
import nextstep.subway.exception.NotFoundPathsException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.line.domain.LineTest.신분당선;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Sections sections;

    @BeforeEach
    void beforeEach() {
        // given
        sections = new Sections();
        sections.add(new Section(신분당선, 역삼역, 양재역, 10));
        sections.add(new Section(신분당선, 양재역, 사당역, 5));
    }

    @DisplayName("경로 찾기")
    @Test
    void enrollPathsTest(){
        // given
        PathFinder pathFinder = new PathFinder().enrollPaths(sections);

        // when
        GraphPath<Station, DefaultWeightedEdge> paths = pathFinder.findPaths(역삼역, 사당역);
        List<Station> stations = paths.getVertexList();

        // then
        assertThat(stations).hasSize(3);
        assertThat(stations).containsExactly(역삼역, 양재역, 사당역);
    }

    @DisplayName("노선에 포함되지 지하철역 검색")
    @Test
    void isNotEnrolledStationsTest() {
        PathFinder pathFinder = new PathFinder().enrollPaths(sections);

        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 잠실역))
                .isInstanceOf(NotEnrollStationInGraphException.class);
    }

    @DisplayName("출발지와 도착지가 같을 경우")
    @Test
    void isEqualsTwoStationsTest() {
        PathFinder pathFinder = new PathFinder().enrollPaths(sections);

        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 역삼역))
                .isInstanceOf(IsEqualsTwoStationsException.class);
    }

    @DisplayName("경로를 못찾았을 경우")
    @Test
    void isNotFoundPathsTest() {
        Station 천호역 = new Station("천호역");
        Line 이호선 = new Line("이호선", "green");
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(신분당선, 역삼역, 양재역, 10));
        sectionList.add(new Section(신분당선, 양재역, 사당역, 5));
        sectionList.add(new Section(이호선, 천호역, 잠실역, 10));
        Sections actual = new Sections(sectionList);

        PathFinder pathFinder = new PathFinder().enrollPaths(actual);

        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 천호역))
                .isInstanceOf(NotFoundPathsException.class);
    }

}
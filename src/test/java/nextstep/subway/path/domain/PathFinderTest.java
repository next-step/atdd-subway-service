package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.CyclePathException;
import nextstep.subway.common.exception.NotFoundEntityException;
import nextstep.subway.common.exception.UnconnectedStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("경로 조회 도메인 관련")
class PathFinderTest {
    private Line 신분당선, 이호선, 삼호선, 사호선;
    private Station 강남역, 양재역, 교대역, 남부터미널역, 이촌역, 삼각지역, 새로운역1, 도곡역, 수서역, 가락시장;
    private List<Section> 구간들;
    private LoginMember 아이멤버, 청소년멤버, 어른멤버;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        이촌역 = new Station("이촌역");
        삼각지역 = new Station("삼각지역");
        새로운역1 = new Station("새로운역1");
        도곡역 = new Station("도곡역");
        수서역 = new Station("수서역");
        가락시장 = new Station("가락시장");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 500);
        사호선 = new Line("사호선", "bg-red-600", 이촌역, 삼각지역, 5);

        삼호선.addLineStation(교대역, 남부터미널역, 3);
        구간들 = Arrays.asList(
                new Section(신분당선, 강남역, 양재역, 10),
                new Section(이호선, 교대역, 강남역, 10),
                new Section(삼호선, 교대역, 남부터미널역, 3),
                new Section(삼호선, 남부터미널역, 양재역, 2),
                new Section(사호선, 이촌역, 삼각지역, 5),
                new Section(삼호선, 양재역, 도곡역, 3),
                new Section(삼호선, 도곡역, 수서역, 23),
                new Section(삼호선, 수서역, 가락시장, 43)
        );

        아이멤버 = new LoginMember(1L, "test@test.com", 8);
        청소년멤버 = new LoginMember(2L, "test22@test22.com", 16);
        어른멤버 = new LoginMember(3L, "test3@test23333.com", 46);
    }

    @DisplayName("최단 경로를 찾을 수 있다. (비회원 검색 - 거리 12km)")
    @Test
    void findShortestPathSuccessNoLogin() {
        PathFinder pathFinder = new PathFinder(강남역, 남부터미널역, 구간들);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath();
        assertAll(
                () -> assertThat(shortestPath).isNotNull(),
                () -> assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(
                        Arrays.asList(강남역, 양재역, 남부터미널역)),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(12.0),
                () -> assertThat(pathFinder.getTotalFee()).isEqualTo(2250)
        );
    }

    @DisplayName("최단 경로를 찾을 수 있다. (회원 검색 - 아이, 거리 10km)")
    @Test
    void findShortestPathSuccessKidLogin() {
        PathFinder pathFinder = new PathFinder(강남역, 양재역, 구간들, 아이멤버);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath();
        assertAll(
                () -> assertThat(shortestPath).isNotNull(),
                () -> assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(
                        Arrays.asList(강남역, 양재역)),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(10.0),
                () -> assertThat(pathFinder.getTotalFee()).isEqualTo(900)
        );
    }

    @DisplayName("최단 경로를 찾을 수 있다. (회원 검색 - 청소년, 거리 79km)")
    @Test
    void findShortestPathSuccessAdolescentLogin() {
        PathFinder pathFinder = new PathFinder(강남역, 가락시장, 구간들, 청소년멤버);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath();
        assertAll(
                () -> assertThat(shortestPath).isNotNull(),
                () -> assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(
                        Arrays.asList(강남역, 양재역, 도곡역, 수서역, 가락시장)),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(79.0),
                () -> assertThat(pathFinder.getTotalFee()).isEqualTo(2160)
        );
    }

    @DisplayName("최단 경로를 찾을 수 있다. (회원 검색 - 어른, 거리 79km)")
    @Test
    void findShortestPathSuccessAdultLogin() {
        PathFinder pathFinder = new PathFinder(강남역, 가락시장, 구간들, 어른멤버);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath();
        assertAll(
                () -> assertThat(shortestPath).isNotNull(),
                () -> assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(
                        Arrays.asList(강남역, 양재역, 도곡역, 수서역, 가락시장)),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(79.0),
                () -> assertThat(pathFinder.getTotalFee()).isEqualTo(3050)
        );
    }

    @DisplayName("출발역과 종착역이 같은 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionSameStation() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 강남역, 구간들, 아이멤버);

        }).isInstanceOf(CyclePathException.class)
        .hasMessageContaining("출발역과 종착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionUnConnected() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 이촌역, 구간들, 아이멤버);
            pathFinder.findShortestPath();

        }).isInstanceOf(UnconnectedStationException.class)
                .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 최단 경로를 찾을 수 없다.")
    @Test
    void findShortestPathExceptionNotFoundStation() {
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(강남역, 새로운역1, 구간들, 아이멤버);
            pathFinder.findShortestPath();

        }).isInstanceOf(NotFoundEntityException.class)
                .hasMessageContaining("Entity를 찾지 못했습니다.");
    }
}
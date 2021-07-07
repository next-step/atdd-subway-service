package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("최단 경로 서비스 관련 테스트")
@SpringBootTest
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L,"강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 남부터미널역, 교대역, 10);
        신분당선 = new Line("신분당선", "pink", 강남역, 양재역, 10);
        삼호선.addStation(양재역,남부터미널역,10);
    }


    @DisplayName("Jgraph를 사용하여 최단 경로와 거리를 확인한다")
    @Test
    void generateShortestPathTest() {
        //when
        GraphPath<Station, DefaultWeightedEdge> stationPath = pathService.generateShortestPath(강남역, 남부터미널역, Arrays.asList(이호선,삼호선,신분당선));

        //then
        assertThat(stationPath.getVertexList()).containsExactly(강남역,교대역,남부터미널역);
    }

    @DisplayName("Jgraph를 사용하여 최단 경로와 거리를 확인한다 -길이가 같은 경우 먼저 입력된 순(빠른라인순)")
    @Test
    void generateShortestPathWhenSameDistanceTest() {

        //when
        GraphPath<Station, DefaultWeightedEdge> stationPath = pathService.generateShortestPath(강남역, 남부터미널역, Arrays.asList(신분당선,삼호선,이호선));

        //then
        assertThat(stationPath.getVertexList()).containsExactly(강남역,양재역,남부터미널역);
    }


    @DisplayName("경로가 존재하지 않을때 실패")
    @Test
    void generateShortestPathFailedBecauseOfNotExistPathTest() {
        //give
        Station 종로역 = new Station(6L, "종로역");
        Station 동묘역 = new Station(7L, "동묘역");
        Line 일호선 = new Line("일호선", "blue", 종로역, 동묘역, 1);

        //when && then
        assertThatThrownBy(() -> pathService.generateShortestPath(강남역, 종로역, Arrays.asList(신분당선,삼호선,이호선,일호선)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("연결된 경로가 없습니다.");
    }

}

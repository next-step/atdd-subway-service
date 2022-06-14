package nextstep.subway.line.domain.collections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.constant.MemberFarePolicy;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.vo.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선목록 이용 관련 단위테스트")
class LinesTest {

    private final Station 구로 = new Station("구로");
    private final Station 독산 = new Station("독산");
    private final Station 신풍 = new Station("신풍");
    private final Station 신림 = new Station("신림");
    private final Station 신도림 = new Station("신도림");
    private final Station 남구로 = new Station("남구로");
    private final Station 가산디지털단지 = new Station("가산디지털단지");
    private final Station 구로디지털단지 = new Station("구로디지털단지");
    private final Station 강남 = new Station("강남");
    private final Station 판교 = new Station("판교");

    private final Line 일호선 = new Line("일호선", "bg-blue-100", 신도림, 구로, 5);
    private final Line 이호선 = new Line("이호선", "bg-green-100", 신도림, 신풍, 10);
    private final Line 칠호선 = new Line("칠호선", "bg-dark-green-100", 가산디지털단지, 남구로, 5);
    private final Line 신분당선 = new Line("신분당선", "bg-red-100", 강남, 판교, 5, 900);

    @BeforeEach
    void setUp() {
        일호선.addNewSection(구로, 가산디지털단지, 15);
        일호선.addNewSection(가산디지털단지, 독산, 5);
        이호선.addNewSection(신풍, 신림, 10);
        이호선.addNewSection(신림, 강남, 15);
        칠호선.addNewSection(남구로, 신풍, 5);
    }

    @DisplayName("출발역과 도착역 사이의 경유역들과 최단거리를 조회한다.")
    @Test
    void findShortestPathV2() {

        //when
        Lines lines = new Lines(Arrays.asList(일호선, 이호선, 칠호선, 신분당선));
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPath(독산, 신림);
        List<Station> routes = shortestPath.getVertexList();

        //then
        List<String> pathStationNames = routes.stream().map(Station::getName).collect(Collectors.toList());
        assertThat(pathStationNames).containsExactly("독산", "가산디지털단지", "남구로", "신풍", "신림");
        assertThat((int) shortestPath.getWeight()).isEqualTo(25);
    }

    @DisplayName("최단경로(+거리)의 요금을 조회한다.")
    @Test
    void calculateFare() {

        //given
        Lines lines = new Lines(Arrays.asList(일호선, 이호선, 칠호선, 신분당선));
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPath(독산, 신림);

        //when
        Fare fare = lines.calculateFare(shortestPath, MemberFarePolicy.GENERAL);

        //then
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

}

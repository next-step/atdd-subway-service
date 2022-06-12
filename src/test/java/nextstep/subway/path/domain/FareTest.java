package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.collections.Lines;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.vo.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("요금 관련 단위테스트")
@DataJpaTest
class FareTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

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
    private Lines lines;


    @BeforeEach
    void setUp() {
        일호선.addNewSection(구로, 가산디지털단지, 15);
        일호선.addNewSection(가산디지털단지, 독산, 5);
        이호선.addNewSection(신풍, 신림, 10);
        이호선.addNewSection(신림, 강남, 15);
        칠호선.addNewSection(남구로, 신풍, 5);

        stationRepository.saveAll(Arrays.asList(독산, 구로디지털단지));
        lineRepository.saveAll(Arrays.asList(일호선, 이호선, 칠호선, 신분당선));
        lines = new Lines(Arrays.asList(일호선, 이호선, 칠호선, 신분당선));
    }

    @DisplayName("요금을 계산한다.(기본요금)")
    @Test
    void calcFare(){
        //given
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPathV2(독산, 가산디지털단지);
        Fare fare = new Fare(shortestPath, null);

        //when
        int finalFare = fare.calcFare();

        //then
        assertThat(finalFare).isEqualTo(1250);
    }

    @DisplayName("요금을 계산한다.(거리 추가요금)")
    @Test
    void calcFare_extra_distance(){
        //given
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPathV2(독산, 구로);
        Fare fare = new Fare(shortestPath, null);

        //when
        int finalFare = fare.calcFare();

        //then
        assertThat(finalFare).isEqualTo(1550);
    }

    @DisplayName("요금을 계산한다.(노선 추가요금)")
    @Test
    void calcFare_extra_charge(){
        //given
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPathV2(강남, 판교);
        Fare fare = new Fare(shortestPath, null);

        //when
        int finalFare = fare.calcFare();

        //then
        assertThat(finalFare).isEqualTo(2150);
    }

    @DisplayName("요금을 계산한다.(할인요금)")
    @Test
    void calcFare_discount(){
        //given
        GraphPath<Station, SectionEdge> shortestPath = lines.findShortestPathV2(강남, 판교);
        Member 청소년 = new Member("teenager@test.com","1234",15);
        Member 어린이 = new Member("teenager@test.com","1234",9);
        Fare fare1 = new Fare(shortestPath, 청소년);
        Fare fare2 = new Fare(shortestPath, 어린이);

        //when
        int teenagerFare = fare1.calcFare();
        int childFare = fare2.calcFare();

        //then
        assertThat(teenagerFare).isEqualTo(1440);
        assertThat(childFare).isEqualTo(900);
    }


}

package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.collections.Lines;
import nextstep.subway.member.constant.MemberFarePolicy;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShortestPathTest {

    private final Station 구로 = new Station("구로");
    private final Station 독산 = new Station("독산");
    private final Station 신풍 = new Station("신풍");
    private final Station 신림 = new Station("신림");
    private final Station 신도림 = new Station("신도림");
    private final Station 남구로 = new Station("남구로");
    private final Station 가산디지털단지 = new Station("가산디지털단지");
    private final Station 강남 = new Station("강남");
    private final Station 판교 = new Station("판교");
    private final Station 잠실 = new Station("잠실");
    private final Station 수지 = new Station("수지");

    private final Line 일호선 = new Line("일호선", "bg-blue-100", 신도림, 구로, 5);
    private final Line 이호선 = new Line("이호선", "bg-green-100", 신도림, 신풍, 10);
    private final Line 칠호선 = new Line("칠호선", "bg-dark-green-100", 가산디지털단지, 남구로, 5);
    private final Line 신분당선 = new Line("신분당선", "bg-red-100", 강남, 판교, 5, 900);
    private final Line 분당선 = new Line("분당선", "bg-red-100", 판교, 수지, 14, 500);
    private Lines lines;


    @BeforeEach
    void setUp() {
        일호선.addNewSection(구로, 가산디지털단지, 15);
        일호선.addNewSection(가산디지털단지, 독산, 5);
        이호선.addNewSection(신풍, 신림, 10);
        이호선.addNewSection(신림, 강남, 15);
        이호선.addNewSection(강남, 잠실, 30);
        칠호선.addNewSection(남구로, 신풍, 5);

        lines = new Lines(Arrays.asList(일호선, 이호선, 칠호선, 신분당선, 분당선));
    }

    @DisplayName("요금을 계산한다.(기본요금)")
    @Test
    void calculateFare() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(독산, 가산디지털단지));

        //when
        int finalFare = shortestPath.calculateFare(MemberFarePolicy.GENERAL);

        //then
        assertThat(finalFare).isEqualTo(1250);
    }

    @DisplayName("요금을 계산한다.(거리 추가요금)")
    @Test
    void calculateFare_extra_distance() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(독산, 구로));

        //when
        int finalFare = shortestPath.calculateFare(MemberFarePolicy.GENERAL);

        //then
        assertThat(finalFare).isEqualTo(1450);
    }

    @DisplayName("요금을 계산한다.(거리 추가요금 50KM 초과)")
    @Test
    void calculateFare_extra_distance_over_50KM() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(독산, 잠실));

        //when
        int finalFare = shortestPath.calculateFare(MemberFarePolicy.GENERAL);

        //then
        assertThat(finalFare).isEqualTo(2350);
    }

    @DisplayName("요금을 계산한다.(노선 추가요금)")
    @Test
    void calcFare_extra_charge() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(강남, 판교));
        //when
        int finalFare = shortestPath.calculateFare(MemberFarePolicy.GENERAL);

        //then
        assertThat(finalFare).isEqualTo(2150);
    }

    @DisplayName("요금을 계산한다.(할인요금)")
    @Test
    void calculateFare_discount() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(강남, 판교));

        //when
        int teenagerFare = shortestPath.calculateFare(MemberFarePolicy.TEENAGER);
        int childFare = shortestPath.calculateFare(MemberFarePolicy.CHILD);

        //then
        assertThat(teenagerFare).isEqualTo(1440);
        assertThat(childFare).isEqualTo(900);
    }

    @DisplayName("요금을 계산한다.(복합)")
    @Test
    void calculateFare_complex() {
        //given
        ShortestPath shortestPath = new ShortestPath(lines.findShortestPath(독산, 수지));

        //when
        int generalFare = shortestPath.calculateFare(MemberFarePolicy.GENERAL);
        int teenagerFare = shortestPath.calculateFare(MemberFarePolicy.TEENAGER);
        int childFare = shortestPath.calculateFare(MemberFarePolicy.CHILD);

        //then
        assertThat(generalFare).isEqualTo(3150);
        assertThat(teenagerFare).isEqualTo(2240);
        assertThat(childFare).isEqualTo(1400);
    }

}

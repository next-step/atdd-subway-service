package nextstep.subway.path.application.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MaxLineOverFarePolicyTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 선릉역;
    private Station 방배역;
    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* ---   강남역 --- *2호선* ---   선릉역 --- *2호선* ---   방배역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");
        선릉역 = new Station(5L, "선릉역");
        방배역 = new Station(6L, "방배역");

        신분당선 = new Line(1L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10, 0);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);
        삼호선.addLineStation(교대역, 남부터미널역, 3);
        이호선.addLineStation(강남역, 선릉역, 20);
        이호선.addLineStation(선릉역, 방배역, 50);
        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("900원 추가 노선")
    @Test
    void getMaxLineOverFare1() {
        //given
        //when
        GraphPath<Station, Section> path = pathFinder.getPath(강남역, 양재역);
        MaxLineOverFarePolicy maxLineOverFarePolicy = new MaxLineOverFarePolicy(path.getEdgeList());

        //then
        int fare = maxLineOverFarePolicy.calculateFare(1250);
        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("300원 추가 노선")
    @Test
    void getMaxLineOverFare2() {
        //given
        //when
        GraphPath<Station, Section> path = pathFinder.getPath(남부터미널역, 양재역);
        MaxLineOverFarePolicy maxLineOverFarePolicy = new MaxLineOverFarePolicy(path.getEdgeList());

        //then
        int fare = maxLineOverFarePolicy.calculateFare(1250);
        assertThat(fare).isEqualTo(1550);
    }

    @DisplayName("추가금 없는 노선")
    @Test
    void getMaxLineOverFare3() {
        //given
        //when
        GraphPath<Station, Section> path = pathFinder.getPath(교대역, 선릉역);
        MaxLineOverFarePolicy maxLineOverFarePolicy = new MaxLineOverFarePolicy(path.getEdgeList());

        //then
        int fare = maxLineOverFarePolicy.calculateFare(1250);
        assertThat(fare).isEqualTo(1250);
    }
}
package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역      --- *2호선* ---   강남역
     * |                            |
     * *3호선*                    *신분당선*
     * |                            |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void before() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L,"교대역");
        남부터미널역 = new Station(4L,"남부터미널역");

        신분당선 = new Line("신분당선","red", 강남역, 양재역, 10, 900);
        이호선 = new Line("이호선","green", 교대역, 강남역, 10, 600);
        삼호선 = new Line("삼호선","orange", 교대역, 양재역, 5, 500);

        Section 삼호선_새로운구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
        삼호선.addSection(삼호선_새로운구간);
    }

    @Test
    @DisplayName("경로를 조회 할때 노선의 추가 요금에서 가장 큰 금액이 할증 된다.")
    void fareTest01() {
        //when : 강남역에서 남부터미널로 가는 최단거리 검색
        PathFinder finder = new DijkstraPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
        Path path = finder.findShortest(강남역, 남부터미널역);


        Fare fare = new Fare(path);
        int totalFare = fare.getTotalFare();
        //거리에 비례한 요금(1350)과 노선에 할증 요금(900)이 합쳐서 운임 요금이 결산
        assertThat(path.getShortestDistance()).isEqualTo(12);
        assertThat(totalFare).isEqualTo(2250);
    }
}

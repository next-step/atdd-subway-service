package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private final DistanceFarePolicy distancePolicy = new DistanceFarePolicy();
    private final LineFarePolicy linePolicy = new LineFarePolicy();
    private final AgeFarePolicy ageFarePolicy = new AgeFarePolicy();
    private final FareCalculator fareCalculator = new FareCalculator(distancePolicy, linePolicy, ageFarePolicy);
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "아무색", 강남역, 양재역, 10, Fare.from(900));
        이호선 = new Line("이호선", "파랑색", 교대역, 강남역, 10, Fare.from(500));
        삼호선 = new Line("삼호선", "주황색", 교대역, 양재역, 5, Fare.from(100));

    }

    /**
     * when 10살 어린이가 기본요금 1250원에 추가요금 900원이 있는 노선을 20KM 이용하면
     * then 총금액은 1350원의 요금이 나온다.
     * 기본요금(1250원) + 추가요금(900원) + 10KM 추가요금(200원) =2350원
     * 어린이 할인(2350-350)*0.5 = 1000원)
     * 계산요금 = 2350-1000 = 1350원
     */
    @Test
    void 거리_노선_요금계산() {
        Distance distance = Distance.from(20);
        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);
        Fare expected = Fare.from(1350);

        assertThat(fareCalculator.calculate(distance, lines, 10)).isEqualTo(expected);
    }


}

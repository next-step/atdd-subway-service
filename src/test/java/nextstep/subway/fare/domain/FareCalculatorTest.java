package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FareCalculatorTest {
    private FareCalculator fareCalculator;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private int age;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .upStation(강남역)
                .downStation(양재역)
                .distance(Distance.from(10))
                .extraFare(Fare.from(900))
                .build();
        이호선 = new Line.Builder("이호선", "bg-red-600")
                .upStation(교대역)
                .downStation(강남역)
                .distance(Distance.from(10))
                .extraFare(Fare.from(0))
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-red-600")
                .upStation(교대역)
                .downStation(양재역)
                .distance(Distance.from(5))
                .extraFare(Fare.from(500))
                .build();

        fareCalculator = new FareCalculator(new DistanceFarePolicy(), new LineFarePolicy(), new AgeFarePolicy());
    }


    /**
     * when 30Km 거리이고
     * <p>
     * And 900원의 노선 추가 요금이 있고
     * <p>
     * And 어린이 이면
     * <p>
     * Then (1650 + 900) - (1650 + 900 - 350) * 0.5 의 요금이 나온다
     */
    @Test
    void 요금_계산() {

        Fare actual = fareCalculator.calculate(Distance.from(30), Arrays.asList(신분당선, 이호선, 삼호선), 8);
        Fare expected = Fare.from(1450);

        assertThat(actual).isEqualTo(expected);
    }
}
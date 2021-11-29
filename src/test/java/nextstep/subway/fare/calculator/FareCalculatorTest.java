package nextstep.subway.fare.calculator;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.path.finder.DijkstraShortestPathAlgorithm;
import nextstep.subway.path.finder.PathFinder;
import nextstep.subway.station.domain.Station;

class FareCalculatorTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_3 = 3;
    private static final int FARE_1000 = 1000;
    private static final int FARE_900 = 900;
    private static final int FARE_800 = 800;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void beforeEach() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "RED", FARE_1000, 강남역, 양재역, DISTANCE_10);
        이호선 = Line.of("이호선", "GREED", FARE_900, 교대역, 강남역, DISTANCE_10);
        삼호선 = Line.of("삼호선", "ORANGE", FARE_800, 남부터미널역, 양재역, DISTANCE_5);
        삼호선.addSection(Section.of(교대역, 남부터미널역, Distance.from(DISTANCE_3)));
    }

    @DisplayName("0 ~ 10km 이하 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,1250", "5,1250", "9,1250", "10,1250"})
    void calculate1(int distance, int fare) {
        // when
        int actualFare = FareCalculator.calculatePathFare(distance);

        // then
        assertThat(actualFare).isEqualTo(fare);
    }

    @DisplayName("10km 초과 ~ 50km 이하 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "18,1450", "45,1950", "46,2050", "50,2050"})
    void calculate2(int distance, int expectedFare) {
        // when
        int actualFare = FareCalculator.calculatePathFare(distance);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("50km 초과 거리구간의 기준 요금 조회를 한다.")
    @ParameterizedTest
    @CsvSource(value = {"51,2150", "58,2150", "59,2250", "66,2250", "90,2550", "95,2650", "100,2750"})
    void calculate3(int distance, int expectedFare) {
        // when
        int actualFare = FareCalculator.calculatePathFare(distance);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("추가요금이 있는 노선을 지나는 최단경로에서, 가장 큰 노선 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"500,1,800", "900,1,900", "1000,1,1000", "1500,10, 1500", "5000,60, 5000"})
    void calculateLineFare1(int lineFare, int distance, int actualFare) {
        // given
        Station 모란역 = Station.of(5L, "모란역");
        Line 분당선 = Line.of("분당선", "YELLOW", lineFare, 양재역, 모란역, distance);

        PathFinder pathFinder = PathFinder.from(DijkstraShortestPathAlgorithm.from(Arrays.asList(신분당선, 이호선, 삼호선, 분당선)));
        ShortestPath shortestPath = pathFinder.findShortestPath(교대역, 모란역);

        // when
        int fare = FareCalculator.calculateLineFare(Arrays.asList(신분당선, 이호선, 삼호선, 분당선), shortestPath);

        // then
        assertThat(actualFare).isEqualTo(fare);
    }
}
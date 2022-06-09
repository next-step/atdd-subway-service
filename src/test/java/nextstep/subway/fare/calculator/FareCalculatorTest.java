package nextstep.subway.fare.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.FareSectionType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareCalculatorTest {

    private static final int NBD_LINE_FARE = 1500;
    private static final int TRD_LINE_FARE = 500;

    private Line 신분당선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "RED", 강남역, 양재역, 60, NBD_LINE_FARE);
        삼호선 = Line.of("삼호선", "ORANGE", 남부터미널역, 양재역, 40, TRD_LINE_FARE);
        삼호선.addSection(Section.of(교대역, 남부터미널역, 3));
    }

    @DisplayName("10km 이하 거리는 기본 요금이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 5, 6, 9, 10})
    void calculate01(int distance) {
        // given & when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);
        int overFare = FareCalculator.calculateOverFare(distance, fareSectionType);

        // then
        assertThat(overFare).isEqualTo(0);
    }

    @DisplayName("10km 초과 50km 까지의 거리는 초과한 거리 5km 당 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource(value = {"11:100", "35:500", "50:800"}, delimiter = ':')
    void calculate02(int distance, int additionalFare) {
        // given & when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);
        int overFare = FareCalculator.calculateOverFare(distance, fareSectionType);

        // then
        assertThat(overFare).isEqualTo(additionalFare);
    }

    @DisplayName("50km 초과 초과한 거리 8km 당 100원이 추가된다.")
    @ParameterizedTest
    @CsvSource(value = {"51:600", "70:800", "90:1000"}, delimiter = ':')
    void calculate03(int distance, int additionalFare) {
        // given & when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);
        int overFare = FareCalculator.calculateOverFare(distance, fareSectionType);

        // then
        assertThat(overFare).isEqualTo(additionalFare);
    }

    @DisplayName("노선에 운임료가 있는 경우 가장 높은 운임료가 추가된다.")
    @Test
    void calculate04() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(남부터미널역, 강남역);

        // when
        int lineFare = FareCalculator.calculateLineFare(lines, path);

        // then
        /**
         *  남부터미널 -<3호선>- 양재역 - <신분당선> - 강남역
         */
        assertThat(lineFare).isEqualTo(NBD_LINE_FARE);
    }
}
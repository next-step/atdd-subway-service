package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ShortestPathTest {
    private static final int BASIC_PRICE = 1250;

    @DisplayName("추가 요금이 없을 때의 기본 지하철 요금은 1250원이다.")
    @Test
    void calculateBasicFare() {
        // given
        Line line = new Line("신분당선", "bg-red-600", new Fare(0));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(33);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE);
    }

    @DisplayName("추가 요금이 있을경우, 지하철 요금은 기본요금과 추가요금의 합이다.")
    @Test
    void calculateExtraFare() {
        // given
        int extraFare = 1_000;
        Line line = new Line("신분당선", "bg-red-600", new Fare(extraFare));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(33);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare);
    }

    @DisplayName("환승의 경우 추가 요금이 가장 큰 라인의 추가 요금이 적용된다.")
    @Test
    void calculateMultiExtraFare() {
        // given
        int extraFare = 1_000;
        Line line = new Line("신분당선", "bg-red-600", new Fare(extraFare));
        Line line2 = new Line("일호선", "bg-red-400", new Fare(300));
        Lines lines = new Lines(Arrays.asList(line, line2));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(33);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare);
    }

    @DisplayName("거리에 따라 요금 정책이 적용된다")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "20:1450", "45:1950", "50:2050", "58:2150", "178:3650"}, delimiter = ':')
    void calculateExtraFareByDistance(int distance, int fare) {
        // given
        Line line = new Line("신분당선", "bg-red-600", new Fare(0));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), distance, lines);

        // when
        Fare result = path.calculateFare(33);

        // then
        assertThat(result.get()).isEqualTo(fare);
    }

    @DisplayName("어린이는 지하철 요금의 50%를 할인받는다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void calculateDiscountChild(int age) {
        // given
        int extraFare = 1_000;
        int expectDiscount = 950;
        Line line = new Line("신분당선", "bg-red-600", new Fare(extraFare));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(age);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare - expectDiscount);
    }

    @DisplayName("청소년은 지하철 요금의 20%를 할인받는다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void calculateDiscountTeenager(int age) {
        // given
        int extraFare = 1_000;
        int expectDiscount = 380;
        Line line = new Line("신분당선", "bg-red-600", new Fare(extraFare));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(age);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare - expectDiscount);
    }

    @DisplayName("어린이와 청소년이 아닐 경우, 할인이 적용되지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 21, 99})
    void calculateNoDiscount(int age) {
        // given
        int extraFare = 1_000;
        Line line = new Line("신분당선", "bg-red-600", new Fare(extraFare));
        Lines lines = new Lines(Arrays.asList(line));
        ShortestPath path = new ShortestPath(new ArrayList<>(), 0, lines);

        // when
        Fare result = path.calculateFare(age);

        // then
        assertThat(result.get()).isEqualTo(BASIC_PRICE + extraFare);
    }
}

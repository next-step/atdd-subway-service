package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

class FareCalculatorTest {
    private static Line 신분당선;
    private static Line 이호선;

    @BeforeEach
    void setUp() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 양재역 = new Station(2L, "양재역");
        final Station 교대역 = new Station(3L, "교대역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10, 300);
        이호선 = new Line("2호선", "green", 교대역, 강남역, 10, 500);
    }

    @DisplayName("거리와 노선별 추가요금이 적절히 적용된 요금이 반환되는지 테스트")
    @ParameterizedTest
    @CsvSource(value = {"11:1850", "9:1750"}, delimiter = ':')
    void calculateFare(int distance, int expectedFare) {
        // given
        final List<Line> lines = Arrays.asList(신분당선, 이호선);

        // when
        final int fare = FareCalculator.calculateFare(lines, distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}

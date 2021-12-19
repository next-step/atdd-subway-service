package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {
    List<Line> lines = new ArrayList();
    private Station 강남역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

    }

    @DisplayName("12km인 경우 요금을 테스트한다")
    @Test
    void calculateTest() {
        // given
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 남부터미널역, 2);
        lines.add(이호선);
        lines.add(삼호선);

        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        Path path = jGraphTPathFinder.findPath(lines, 강남역.getId(), 남부터미널역.getId());
        assertThat(path.getDistance()).isEqualTo(12);
        FareCalculator fareCalculator = new FareCalculator();

        // when
        BigDecimal fare = fareCalculator.calculate(lines, path);

        // then
        assertThat(fare).isEqualTo(new BigDecimal(1350));
    }

    @DisplayName("70km인 경우 요금을 테스트한다")
    @Test
    void calculateTest2() {
        // given
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 남부터미널역, 60);
        lines.add(이호선);
        lines.add(삼호선);

        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        Path path = jGraphTPathFinder.findPath(lines, 강남역.getId(), 남부터미널역.getId());
        assertThat(path.getDistance()).isEqualTo(70);
        FareCalculator fareCalculator = new FareCalculator();

        // when
        BigDecimal fare = fareCalculator.calculate(lines, path);

        // then
        assertThat(fare).isEqualTo(new BigDecimal(2350));
    }

    @DisplayName("5km인 경우 요금을 테스트한다")
    @Test
    void calculateTest3() {
        // given
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 3);
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 남부터미널역, 2);
        lines.add(이호선);
        lines.add(삼호선);

        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        Path path = jGraphTPathFinder.findPath(lines, 강남역.getId(), 남부터미널역.getId());
        assertThat(path.getDistance()).isEqualTo(5);
        FareCalculator fareCalculator = new FareCalculator();

        // when
        BigDecimal fare = fareCalculator.calculate(lines, path);

        // then
        assertThat(fare).isEqualTo(new BigDecimal(1250));
    }
}

package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChargeTest {
    @Test
    @DisplayName("거리 및 노선 요금 계산 - 노선 2개")
    void chargeWithDistanceAndLine1(){
        // given
        int distance = 15;
        Set<Line> lines = new HashSet<>();
        lines.add(new Line("2호선", "green", new Station("강남역"), new Station("신림역"), 50, 200));
        lines.add(new Line("5호선", "purple", new Station("신길역"), new Station("강동역"), 50, 500));

        // when
        Charge charge = new Charge(distance, lines);

        // then
        assertThat(charge.value()).isEqualTo(1850);
    }

    @Test
    @DisplayName("거리 및 노선 요금 계산 - 거리 긴 경우")
    void chargeWithDistanceAndLine2(){
        // given
        int distance = 66;
        Set<Line> lines = new HashSet<>();
        lines.add(new Line("2호선", "green", new Station("강남역"), new Station("신림역"), 50, 200));
        lines.add(new Line("5호선", "purple", new Station("신길역"), new Station("강동역"), 50, 500));
        lines.add(new Line("3호선", "purple", new Station("교대역"), new Station("일산역"), 50, 300));

        // when
        Charge charge = new Charge(distance, lines);

        // then
        assertThat(charge.value()).isEqualTo(2750);
    }

    @Test
    @DisplayName("거리, 노선, 나이 요금 계산 - 청소년의 경우")
    void chargeWithDistanceAndLineAndAge(){
        // given
        int distance = 66;
        Set<Line> lines = new HashSet<>();
        lines.add(new Line("2호선", "green", new Station("강남역"), new Station("신림역"), 50, 200));
        lines.add(new Line("5호선", "purple", new Station("신길역"), new Station("강동역"), 50, 500));
        lines.add(new Line("3호선", "purple", new Station("교대역"), new Station("일산역"), 50, 300));
        Charge charge = new Charge(distance, lines);

        // when
        charge.discount(18);

        // then
        assertThat(charge.value()).isEqualTo(1920);
    }

    @Test
    @DisplayName("거리, 노선, 나이 요금 계산 - 유소년의 경우")
    void chargeWithDistanceAndLineAndAge2(){
        // given
        int distance = 66;
        Set<Line> lines = new HashSet<>();
        lines.add(new Line("2호선", "green", new Station("강남역"), new Station("신림역"), 50, 200));
        lines.add(new Line("5호선", "purple", new Station("신길역"), new Station("강동역"), 50, 500));
        lines.add(new Line("3호선", "purple", new Station("교대역"), new Station("일산역"), 50, 300));
        Charge charge = new Charge(distance, lines);

        // when
        charge.discount(10);

        // then
        assertThat(charge.value()).isEqualTo(1200);
    }
}

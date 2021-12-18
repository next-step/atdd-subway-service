package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FareTest {

    /* ------------
     * 거리별 요금 정책
     * ------------
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */

    private Fare fare;

    @BeforeEach
    void setUp() {
        fare = Fare.of();
    }

    @Test
    void calculateOverFareByDistance_within10km() {
        // given
        final FareDistance distance = new FareDistance(10);

        // when
        final Fare overFare = fare.calculateOverFare(distance);

        // then
        final Fare expected = new Fare(BigDecimal.valueOf(1_250L));
        assertThat(overFare).isEqualTo(expected);
    }

    @Test
    void calculateOverFareByDistance_between10kmTo50km() {
        // given
        final FareDistance distance = new FareDistance(24);

        // when
        final Fare overFare = fare.calculateOverFare(distance);

        // then
        final Fare expected = new Fare(BigDecimal.valueOf(1_450L));
        assertThat(overFare).isEqualTo(expected);
    }

    @Test
    void calculateOverFareByDistance_50kmOrMore() {
        // given
        final FareDistance distance = new FareDistance(64);

        // when
        final Fare overFare = fare.calculateOverFare(distance);

        // then
        final Fare expected = new Fare(BigDecimal.valueOf(1_350L));
        assertThat(overFare).isEqualTo(expected);
    }

    @Test
    void add() {
        // given
        final Fare anotherFare = new Fare(BigDecimal.valueOf(1_000L));

        // when
        final Fare addedFare = fare.add(anotherFare);

        // then
        final Fare expected = new Fare(BigDecimal.valueOf(2_250L));
        assertThat(addedFare).isEqualTo(expected);
    }

    @Test
    void compareTo() {
        // given
        final Fare lessFare = new Fare(BigDecimal.valueOf(1_000L));

        // when
        final int comparison = fare.compareTo(lessFare);

        // then
        assertThat(comparison).isEqualTo(1);
    }

    @Test
    void x() {
        final BigDecimal x = BigDecimal.valueOf(1_000L);
        final BigDecimal y = BigDecimal.valueOf(2_000L);
        final BigDecimal z = x.add(y);

        System.out.println(z);
    }
}

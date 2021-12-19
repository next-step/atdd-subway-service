package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FareTest {

    /* ------------
     * 거리별 요금 정책
     * ------------
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     *

    /* ------------
     * 노선별 요금 정책
     * ------------
     * 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
     * ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
     * ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
     * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
     * ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
     */

    /* ------------
     * 회원 연령별 요금 정책
     * ------------
     * 청소년: 운임에서 350원을 공제한 금액의 20%할인
     * 어린이: 운임에서 350원을 공제한 금액의 50%할인
     * 청소년: 13세 이상~19세 미만
     * 어린이: 6세 이상~ 13세 미만
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
        final Fare expected = new Fare(1_250);
        assertThat(overFare).isEqualTo(expected);
    }

    @Test
    void calculateOverFareByDistance_between10kmTo50km() {
        // given
        final FareDistance distance = new FareDistance(24);

        // when
        final Fare overFare = fare.calculateOverFare(distance);

        // then
        final Fare expected = new Fare(1_450);
        assertThat(overFare).isEqualTo(expected);
    }

    @Test
    void calculateOverFareByDistance_50kmOrMore() {
        // given
        final FareDistance distance = new FareDistance(64);

        // when
        final Fare overFare = fare.calculateOverFare(distance);

        // then
        final Fare expected = new Fare(1_350);
        assertThat(overFare).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void applyDiscount_teenager(final Integer age) {
        // given
        final FareAge fareAge = new FareAge(age);

        // when
        final Fare discounted = fare.applyDiscount(fareAge);

        // then
        final Fare expected = new Fare(720);
        assertThat(discounted).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void applyDiscount_children(final Integer age) {
        // given
        final FareAge fareAge = new FareAge(age);

        // when
        final Fare discounted = fare.applyDiscount(fareAge);

        // then
        final Fare expected = new Fare(450);
        assertThat(discounted).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 19})
    void applyDiscount_notApplicable(final Integer age) {
        // given
        final FareAge fareAge = new FareAge(age);

        // when
        final Fare discounted = fare.applyDiscount(fareAge);

        // then
        final Fare expected = new Fare(1_250);
        assertThat(discounted).isEqualTo(expected);
    }

    @Test
    void add() {
        // given
        final Fare anotherFare = new Fare(1_000);

        // when
        final Fare addedFare = fare.add(anotherFare);

        // then
        final Fare expected = new Fare(2_250);
        assertThat(addedFare).isEqualTo(expected);
    }

    @Test
    void compareTo() {
        // given
        final Fare lessFare = new Fare(1_000);

        // when
        final int comparison = fare.compareTo(lessFare);

        // then
        assertThat(comparison).isEqualTo(1);
    }
}

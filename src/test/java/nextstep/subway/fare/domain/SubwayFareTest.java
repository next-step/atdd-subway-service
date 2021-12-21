package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayFareTest {

    private static final int FARE = 1_000;

    @DisplayName("청소년 요금 할인 정책 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {13, 18})
    void calculateDiscountFare_youthDiscountPolicy(int age) {
        // given
        DiscountPolicy discountPolicy = DiscountPolicy.of(Age.of(age));
        SubwayFare subwayFare = SubwayFare.of(FARE, discountPolicy);

        // when
        int discountFare = subwayFare.calculateDiscountFare();

        // then
        assertThat(discountFare).isEqualTo((int) ((FARE - Discount.YOUTH.getAmount()) * Discount.YOUTH.getPercent()));
    }

    @DisplayName("어린이 요금 할인 정책 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {6, 12})
    void calculateDiscountFare_childDiscountPolicy(int age) {
        // given
        DiscountPolicy discountPolicy = DiscountPolicy.of(Age.of(age));
        SubwayFare subwayFare = SubwayFare.of(FARE, discountPolicy);

        // when
        int discountFare = subwayFare.calculateDiscountFare();

        // then
        assertThat(discountFare).isEqualTo((int) ((FARE - Discount.CHILD.getAmount()) * Discount.CHILD.getPercent()));
    }

    @DisplayName("요금 할인 없음 정책 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {19, 20})
    void calculateDiscountFare_noneDiscountPolicy(int age) {
        // given
        DiscountPolicy discountPolicy = DiscountPolicy.of(Age.of(age));
        SubwayFare subwayFare = SubwayFare.of(FARE, discountPolicy);

        // when
        int discountFare = subwayFare.calculateDiscountFare();

        // then
        assertThat(discountFare).isEqualTo(FARE);
    }
}

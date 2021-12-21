package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountPolicyTest {

    @DisplayName("청소년 요금 할인 정책 생성 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {13, 18})
    void calculateDiscountFare_youthDiscountPolicy(int age) {
        // when & then
        assertThat(DiscountPolicy.of(Age.of(age))).isExactlyInstanceOf(YouthDiscountPolicy.class);
    }

    @DisplayName("어린이 요금 할인 정책 생성 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {6, 12})
    void calculateDiscountFare_childDiscountPolicy(int age) {
        // when & then
        assertThat(DiscountPolicy.of(Age.of(age))).isExactlyInstanceOf(ChildDiscountPolicy.class);
    }

    @DisplayName("요금 할인 없음 정책 생성 테스트 - 유효한 나이")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {19, 20})
    void calculateDiscountFare_noneDiscountPolicy_validAge(Integer age) {
        // when & then
        assertThat(DiscountPolicy.of(Age.of(age))).isExactlyInstanceOf(NoneDiscountPolicy.class);
    }

    @DisplayName("요금 할인 없음 정책 생성 테스트 - 유효하지 않은 나이")
    @Test
    void calculateDiscountFare_noneDiscountPolicy_invalidAge() {
        // when & then
        assertThat(DiscountPolicy.of(null)).isExactlyInstanceOf(NoneDiscountPolicy.class);
    }
}

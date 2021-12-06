package nextstep.subway.policy.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.policy.DiscountPolicyFactory;

@DisplayName("나이에따른 요금 할인 정책 생성관련")
public class DiscountPolicyFactoryTest {
    @DisplayName("연령이 6이상 ~ 13미만은 어린이할인 정책을 생성한다.")
    @ValueSource(ints = {6, 10, 12})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void generate_ChildDiscountPolicy(int age) {
        // when
        // then
        Assertions.assertThat(DiscountPolicyFactory.generate(age)).isInstanceOf(ChildDiscountPolicy.class);
    }

    @DisplayName("연령이 13이상 ~ 19미만은 청소년할인 정책을 생성한다.")
    @ValueSource(ints = {13, 15, 18})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void generate_YouthDiscountPolicy(int age) {
        // when
        // then
        Assertions.assertThat(DiscountPolicyFactory.generate(age)).isInstanceOf(YouthDiscountPolicy.class);
    }

    @DisplayName("연령이 19이상은 미할인 정책 생성한다.")
    @ValueSource(ints = {19, 20, 30})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void generate_NoneDiscountPolicy(int age) {
        // when
        // then
        Assertions.assertThat(DiscountPolicyFactory.generate(age)).isInstanceOf(NoneDiscountPolicy.class);
    }
}

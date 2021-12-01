package nextstep.subway.policy.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.policy.domain.Price;

@DisplayName("청소년할인 정책 관련 기능")
public class YouthDiscountPolicyTest {
    @DisplayName("연령이 13이상 ~ 19미만은 청소년할인 정책을 적용한다.")
    @ValueSource(ints = {13, 15, 18})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void apply_YouthDiscountPolicy(int age) {
        // given
        YouthDiscountPolicy youthDiscountPolicy = new YouthDiscountPolicy();
        Price price = Price.of(1250);

        // when
        Price acceptedDiscountPrice = youthDiscountPolicy.apply(price);

        // then
        Assertions.assertThat(acceptedDiscountPrice).isEqualTo(Price.of(720));
    }
}

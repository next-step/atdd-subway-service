package nextstep.subway.policy.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.policy.domain.Price;

@DisplayName("연령 할인 미적용 정책 관련 기능")
public class NoneDiscountPolicyTest {
    @DisplayName("연령이 19이상 연령 할인이 적용되지 않는다.")
    @ValueSource(ints = {19, 20, 30})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void apply_NoneDiscountPolicy(int age) {
        // given
        NoneDiscountPolicy noneDiscountPolicy = new NoneDiscountPolicy();
        Price price = Price.of(1250);

        // when
        Price acceptedDiscountPrice = noneDiscountPolicy.apply(price);

        // then
        Assertions.assertThat(acceptedDiscountPrice).isEqualTo(Price.of(1250));
    }
}

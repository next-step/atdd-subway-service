package nextstep.subway.policy.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.policy.domain.Price;

@DisplayName("어린이할인 정책 관련 기능")
public class ChildDiscountPolicyTest {
    @DisplayName("연령이 6이상 ~ 13미만은 어린이할인 정책을 적용한다.")
    @ValueSource(ints = {6, 10, 12})
    @ParameterizedTest(name = "[{index}] 할인 정책이 적용될 나이는 {0}이다.")
    void apply_ChildDiscountPolicy(int age) {
        // given
        ChildDiscountPolicy childDiscountPolicy = new ChildDiscountPolicy();
        Price price = Price.of(1250);

        // when
        Price acceptedDiscountPrice = childDiscountPolicy.apply(price);

        // then
        Assertions.assertThat(acceptedDiscountPrice).isEqualTo(Price.of(450));
    }
}

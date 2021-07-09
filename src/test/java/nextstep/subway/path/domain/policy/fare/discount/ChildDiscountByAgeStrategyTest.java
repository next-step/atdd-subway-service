package nextstep.subway.path.domain.policy.fare.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildDiscountByAgeStrategyTest {
    @DisplayName("어린이 할인 확인")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    public void 어린이_할인_확인(int age) throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L, "test@test.com", age);
        ChildDiscountByAgeStrategy discountStrategy = new ChildDiscountByAgeStrategy();

        //when
        int discount = discountStrategy.discountBy(loginMember, new Fare(1250));

        //then
        assertThat(discount).isEqualTo(800);
    }
}

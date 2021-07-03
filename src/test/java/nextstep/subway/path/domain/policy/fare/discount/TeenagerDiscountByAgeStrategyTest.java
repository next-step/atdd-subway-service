package nextstep.subway.path.domain.policy.fare.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TeenagerDiscountByAgeStrategyTest {
    @DisplayName("청소년 할인 확인")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    public void 청소년_할인_확인(int age) throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L, "test@test.com", age);
        TeenagerDiscountByAgeStrategy discountStrategy = new TeenagerDiscountByAgeStrategy();

        //when
        int discount = discountStrategy.discountBy(loginMember, new Fare(1250));

        //then
        assertThat(discount).isEqualTo(530);
    }
}

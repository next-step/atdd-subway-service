package nextstep.subway.path.infra;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Money;
import nextstep.subway.path.domain.AgeDiscount;
import nextstep.subway.path.domain.MemberDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountTest {

    private MemberDiscount memberDiscount;

    @BeforeEach
    void setUp() {
        memberDiscount = new AgeDiscount();
    }

    @DisplayName("어린이의 경우 운임에서 350원을 공제한 금액의 50% 할인한다. (6세 이상~ 13세 미만)")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void kids(int age) {
        // given
        LoginMember kids = new LoginMember(1L, "test@email.com", age);
        Money fee = Money.valueOf(1350);

        // when
        Money discount = memberDiscount.discount(kids, fee);

        // then
        assertThat(discount).isEqualTo(Money.valueOf(500));
    }

    @DisplayName("청소년의 경우 운임에서 350원을 공제한 금액의 20% 할인한다. (13세 이상~19세 미만)")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void teenager(int age) {
        // given
        LoginMember teenager = new LoginMember(1L, "test@email.com", age);
        Money fee = Money.valueOf(1350);

        // when
        Money discount = memberDiscount.discount(teenager, fee);

        // then
        assertThat(discount).isEqualTo(Money.valueOf(200));
    }

    @DisplayName("청소년이나 어린이가 아니면 할인금액을 제공하지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 19})
    void etc(int age) {
        // given
        LoginMember etc = new LoginMember(1L, "test@email.com", age);
        Money fee = Money.valueOf(1350);

        // when
        Money discount = memberDiscount.discount(etc, fee);

        // then
        assertThat(discount).isEqualTo(Money.zero());
    }
}

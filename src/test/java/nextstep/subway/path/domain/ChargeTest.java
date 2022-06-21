package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ChargeTest {
    @DisplayName("기본요금")
    @Test
    void baseCharge() {
        Charge actual = Charge.BASE_CHARGE;
        Charge expected = new Charge(1250);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("추가요금")
    @Test
    void addSurcharge() {
        Charge actual = new Charge(100).addAll(new Surcharge(200), new Surcharge(300));
        Charge expected = new Charge(600);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("청소년 요금할인")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void discountByTeenager(int age) {
        LoginMember teenager = new LoginMember(1L, "teenager@gmail.com", age);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(teenager);
        Charge expected = new Charge(800);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("어린이 요금할인")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void discountByChildren(int age) {
        LoginMember children = new LoginMember(1L, "children@gmail.com", age);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(children);
        Charge expected = new Charge(500);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("성인 요금 미할인")
    @Test
    void discountByAdult() {
        LoginMember adult = new LoginMember(1L, "adult@gmail.com", 19);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(adult);
        Charge expected = new Charge(1350);
        assertThat(actual).isEqualTo(expected);
    }
}

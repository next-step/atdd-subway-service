package nextstep.subway.auth.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginMemberTest {

    @DisplayName("회원 나이에 따라 타입이 정해진다.")
    @ParameterizedTest()
    @CsvSource(value = {"0,NONE", "6,KID", "12,KID", "13,ADOLESCENT", "18,ADOLESCENT", "19,NONE"})
    void findAgeType(int age, LoginMember.AgeType expected) {
        LoginMember loginMember = new LoginMember(1L, "email@email", age);
        assertEquals(expected, loginMember.getAgeType());
    }

    @DisplayName("게스트 회원은 NONE 타입을 가지게 된다.")
    @Test
    void findGuestMemberAgeType() {
        LoginMember loginMember = new LoginMember(true);
        assertEquals(LoginMember.AgeType.NONE, loginMember.getAgeType());
    }

}

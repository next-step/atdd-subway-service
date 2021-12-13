package nextstep.subway.member.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;

class MemberAgeTypeTest {

    @DisplayName("회원 나이에 따라 타입이 정해진다.")
    @ParameterizedTest()
    @CsvSource(value = {"0,NONE", "6,KID", "12,KID", "13,ADOLESCENT", "18,ADOLESCENT", "19,NONE"})
    void findAgeType(int age, MemberAgeType expected) {
        assertEquals(expected, MemberAgeType.getMemberAgeType(age));
    }

    @DisplayName("게스트 회원은 NONE 타입을 가지게 된다.")
    @Test
    void findGuestMemberAgeType() {
        LoginMember loginMember = new LoginMember();
        assertEquals(MemberAgeType.NONE,
            MemberAgeType.getMemberAgeType(loginMember.getAge()));
    }

}

package nextstep.subway.member.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("회원 할인 정책 테스트 클래스")
class MemberDiscountPolicyTest {

    @ParameterizedTest
    @CsvSource(value = { "19:1350", "25:1550", "40:2550", "50:3000", "60:2850" }, delimiter = ':')
    void 어른_할인(int age, int totalFare) {
        LoginMember loginMember = new LoginMember(1L, "testuser@test.com", age);
        assertEquals(totalFare, MemberDiscountPolicy.getFare(loginMember, totalFare));
    }

    @ParameterizedTest
    @CsvSource(value = { "1250:450", "1850:750", "2050:850", "2450:1050" }, delimiter = ':')
    void 어린이_할인(int totalFare, int expected) {
        LoginMember loginMember = new LoginMember(1L, "testuser@test.com", 6);
        assertEquals(expected, MemberDiscountPolicy.getFare(loginMember, totalFare));
    }

    @ParameterizedTest
    @CsvSource(value = { "1250:720", "1850:1200", "2050:1360", "2450:1680" }, delimiter = ':')
    void 청소년_할인(int totalFare, int expected) {
        LoginMember loginMember = new LoginMember(1L, "testuser@test.com", 13);
        assertEquals(expected, MemberDiscountPolicy.getFare(loginMember, totalFare));
    }
}

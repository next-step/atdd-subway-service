package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import nextstep.subway.path.domain.SubwayFare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginMemberTest {

    @ParameterizedTest
    @CsvSource(delimiterString = ":", value = {"6:500", "13:800", "19:1350"})
    void 연령별_할인요금_계산_테스트(String age, String expectedFare) {
        SubwayFare 할인전요금 = SubwayFare.of(1350);
        LoginMember 로그인유저 = new LoginMember(1L, "subway@subway.com",Integer.parseInt(age));
        SubwayFare 할인적용후금액 = 로그인유저.discountFareByAge(할인전요금);
        assertThat(할인적용후금액.getValue()).isEqualTo(Integer.parseInt(expectedFare));
    }
}

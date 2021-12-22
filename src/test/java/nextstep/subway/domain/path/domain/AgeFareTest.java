package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("연령별 요금 할인")
class AgeFareTest {

    @ParameterizedTest
    @CsvSource(value = {"6:800:1250","12:800:1250","13:530:1250","18:530:1250","19:0:1250"}, delimiter = ':')
    @DisplayName("기본 성인 운임 기준으로 연령별 할인 요금 계산")
    void ageFare(int age, int discountFare, int adultDefaultFare) {
        // given
        AgeFare ageFare = new AgeFare(new LoginUser(age));

        // when
        Amount amount = ageFare.calculateDiscount(new Amount(adultDefaultFare));

        // then
        assertThat(amount).isEqualTo(new Amount(discountFare));
    }
}

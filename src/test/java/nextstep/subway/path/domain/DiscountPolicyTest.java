package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DiscountPolicyTest {
    @DisplayName("나이에 따른 할인 금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:13:180", "11:13:200", "51:13:360", "10:6:450", "11:6:500", "51:6:900"}, delimiter = ':')
    void calculateDiscountFare(int distance, int age, int expected) {
        //given
        LoginMember member = new LoginMember(1L, "test@example.com", age);
        FareType fareType = FareType.findByDistance(distance);
        int fare = fareType.calculateByDistance(distance);

        //when
        DiscountPolicy discountPolicy = new AgeDiscountPolicy();
        int actual = discountPolicy.calculate(fare, member);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}

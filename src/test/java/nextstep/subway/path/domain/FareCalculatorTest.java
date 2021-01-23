package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarexCalculatorTest {

    @ParameterizedTest
    @CsvSource(value = {"9:1250", "30:1650", "60:2250"}, delimiter = ':')
    void calculateFare(int distance, int expectedFare) {
        Integer fare = FareCalculator.calculateFare(distance);
        assertThat(expectedFare).isEqualTo(fare);
    }

    @Test
    @DisplayName("라인추가요금 1000 + 60키로 이동시 성인기본요금(1250) + 50키로이내 추가요금(800) + 50키로초과 추가요금(200) = 3250")
    void extraFareWithLineExtraFare() {
        Integer fare = FareCalculator.calculateFare(1000, 60, getMember(20));
        assertThat(fare).isEqualTo(3250);
    }

    @Test
    @DisplayName("70 키로(2350) 청소년요금 = 1600")
    void discountYouth() {
        Integer fare = FareCalculator.calculateFare(0, 70, getMember(13));
        assertThat(fare).isEqualTo(1600);
    }

    @Test
    @DisplayName("70 키로(2350) 어린이 = 1000")
    void discountChild() {
        Integer fare = FareCalculator.calculateFare(0, 70, getMember(6));
        assertThat(fare).isEqualTo(1000);
    }

    private LoginMember getMember(int age) {
        return new LoginMember(1L, "mj@naver.com", age);
    }

}
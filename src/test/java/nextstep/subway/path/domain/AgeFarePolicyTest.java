package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {
    @ParameterizedTest
    @DisplayName("성인 요금 거리가 70, 30, 10일때 각각 1600, 1040, 1,250원이다")
    @CsvSource(value = {"70:2350", "30:1650", "10:1250"}, delimiter = ':')
    void calculateFare(int distance, int expectedFare) {
        int fare = DistanceFarePolicy.getPolicy(distance).calculateFare(distance);
        assertThat(expectedFare).isEqualTo(fare);
    }

    @ParameterizedTest
    @DisplayName("청소년 요금 거리가 70, 30, 10일때 각각 1600, 1040, 720원이다")
    @CsvSource(value = {"70:1600", "30:1040", "10:720"}, delimiter = ':')
    void discountYouth(int distance, int expectedFare) {
        Integer fare = FareCalculator.calculateFare(0, distance, getMember(13));
        assertThat(expectedFare).isEqualTo(fare);
    }

    @ParameterizedTest
    @DisplayName("어린이 요금 거리가 70, 30, 10일때 각각 1600, 1040, 720원이다")
    @CsvSource(value = {"70:1000", "30:650", "10:450"}, delimiter = ':')
    void discountChild(int distance, int expectedFare) {
        Integer fare = FareCalculator.calculateFare(0, distance, getMember(6));
        assertThat(expectedFare).isEqualTo(fare);
    }

    private LoginMember getMember(int age) {
        return new LoginMember(1L, "mj@naver.com", age);
    }
}
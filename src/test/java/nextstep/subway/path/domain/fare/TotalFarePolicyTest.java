package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;

class TotalFarePolicyTest {

    @DisplayName("거리 변경에 따른 전체 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1250", "11,1350", "50, 2050", "51,2150", "57,2150"})
    void calculateFareWhenDistanceChanged(int distance, int fare) {
        FarePolicy<Integer> distancePolicy = FarePolicyFactory.createDistanceFarePolicy();
        FarePolicy<Fare> agePolicy = FarePolicyFactory.createAgeFarePolicy(LoginMember.NOT_LOGIN);
        TotalFarePolicy totalFarePolicy = new TotalFarePolicy(distancePolicy, agePolicy);

        assertThat(totalFarePolicy.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }

    @DisplayName("나이 변경에 따른 전체 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"6, 450", "12, 450", "13,720", "18, 720", "19, 1250"})
    void calculateFareWhenAgeChanged(int age, int fare) {
        FarePolicy<Integer> distanceFairPolicy = FarePolicyFactory.createDistanceFarePolicy();
        LoginMember loginMember = new LoginMember(1L, "email@email.com", age);
        FarePolicy<Fare> ageFairPolicy = FarePolicyFactory.createAgeFarePolicy(loginMember);
        TotalFarePolicy totalFarePolicy = new TotalFarePolicy(distanceFairPolicy, ageFairPolicy);

        assertThat(totalFarePolicy.calculateFare(10)).isEqualTo(Fare.from(fare));
    }
}
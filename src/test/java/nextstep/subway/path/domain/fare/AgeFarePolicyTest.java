package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AgeFarePolicyTest {

    @DisplayName("나이 정책으로 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"6, 450", "12, 450", "13,720", "18, 720", "19, 1250"})
    void calculateFare(int age, int result) {
        FarePolicy<Fare> policy = AgeFarePolicy.findPolicy(age);

        Fare fare = Fare.BASE;
        fare = policy.calculateFare(fare);

        assertThat(fare).isEqualTo(Fare.from(result));
    }

    @DisplayName("아이 요금 계산 정책 가져오기")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void findChildrenPolicy(int age) {
        FarePolicy<Fare> policy = AgeFarePolicy.findPolicy(age);

        assertThat(policy).isEqualTo(AgeFarePolicy.CHILDREN);
    }

    @DisplayName("청소년 요금 계산 정책 가져오기")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void findYouthPolicy(int age) {
        FarePolicy<Fare> policy = AgeFarePolicy.findPolicy(age);

        assertThat(policy).isEqualTo(AgeFarePolicy.YOUTH);
    }
}
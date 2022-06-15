package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeFarePolicyTest {
    @DisplayName("나이별 요금 정책 조회 테스트")
    @ParameterizedTest(name = "나이가 {0}일 때 해당하는 거리 요금 정책 {1} 확인 테스트")
    @CsvSource(value = {"10, CHILDREN_POLICY", "30, NONE_POLICY", "16, TEENAGER_POLICY"})
    void findAgeFarePolicyByAge(int input, AgeFarePolicy ageFarePolicy) {
        AgeFarePolicy ageFarePolicyByAge = AgeFarePolicy.findAgeFarePolicyByAge(Age.valueOf(input));
        assertThat(ageFarePolicyByAge).isEqualTo(ageFarePolicy);
    }

    @DisplayName("나이별 요금 정책 할인 테스트")
    @ParameterizedTest(name = "나이가 {0}일 때 해당하는 거리 요금 정책의 요금 {1} 반환 테스트")
    @CsvSource(value = {"10, 650", "30, 1650", "16, 1040"})
    void discountFare(int input, int expect) {
        Fare fare = Fare.valueOf(1650);
        AgeFarePolicy ageFarePolicyByAge = AgeFarePolicy.findAgeFarePolicyByAge(Age.valueOf(input));
        ageFarePolicyByAge.discountFare(fare);
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }
}

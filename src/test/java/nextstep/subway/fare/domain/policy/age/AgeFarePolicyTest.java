package nextstep.subway.fare.domain.policy.age;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import nextstep.subway.fare.domain.policy.age.impl.AdultAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.ChildAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.FreeAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.TeenagerAgeFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AgeFarePolicyTest {

    @ParameterizedTest(name = "나이가 {0}세일 경우")
    @DisplayName("나이에 따른 요금정책 클래스를 리턴한다.")
    @MethodSource("providerGetAgeFarePolicy_successCase")
    void getAgeFarePolicy(int age, Class<? extends AgeFarePolicy> ageFarePolicyClass) {
        AgeFarePolicy ageFarePolicy = AgeFarePolicyType.getAgeFarePolicyType(age).getPolicy();
        assertThat(ageFarePolicy).isInstanceOf(ageFarePolicyClass);
    }

    @ParameterizedTest(name = "{0}일 경우, 기본요금은 {2}원 이다.")
    @DisplayName("나이에 따른 기본요금을 계산한다.")
    @MethodSource("providerCalculate_successCase")
    void calculate(String name, AgeFarePolicy ageFarePolicy, int expectedFare) {
        assertThat(ageFarePolicy.calculate()).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> providerGetAgeFarePolicy_successCase() {
        return Stream.of(
            Arguments.of(5, FreeAgeFarePolicy.class),
            Arguments.of(10, ChildAgeFarePolicy.class),
            Arguments.of(15, TeenagerAgeFarePolicy.class),
            Arguments.of(25, AdultAgeFarePolicy.class),
            Arguments.of(70, FreeAgeFarePolicy.class)
        );
    }

    private static Stream<Arguments> providerCalculate_successCase() {
        return Stream.of(
            Arguments.of("우대나이", FreeAgeFarePolicy.getInstance(), 0),
            Arguments.of("어린이", ChildAgeFarePolicy.getInstance(), 450),
            Arguments.of("청소년", TeenagerAgeFarePolicy.getInstance(), 720),
            Arguments.of("성인", AdultAgeFarePolicy.getInstance(), 1250)
        );
    }

}

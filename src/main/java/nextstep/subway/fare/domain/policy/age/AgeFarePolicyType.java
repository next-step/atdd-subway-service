package nextstep.subway.fare.domain.policy.age;

import java.util.Arrays;
import nextstep.subway.fare.domain.policy.age.impl.AdultAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.ChildAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.FreeAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.TeenagerAgeFarePolicy;

public enum AgeFarePolicyType {

    FREE(FreeAgeFarePolicy.class),
    ADULT(AdultAgeFarePolicy.class),
    CHILD(ChildAgeFarePolicy.class),
    Teenager(TeenagerAgeFarePolicy.class);

    private final Class<? extends AgeFarePolicy> ageFarePolicy;
    AgeFarePolicyType(Class<? extends AgeFarePolicy> ageFarePolicy) {
        this.ageFarePolicy = ageFarePolicy;
    }

    public static AgeFarePolicy getAgeFarePolicy(int age) {
        return Arrays.stream(values())
            .map(AgeFarePolicyType::getAgeFarePolicyInstance)
            .filter(policy -> policy.isAge(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("나이에 대한 요금정책이 존재하지 않습니다."));
    }

    private static AgeFarePolicy getAgeFarePolicyInstance(AgeFarePolicyType type) {
        try {
            return type.ageFarePolicy.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}

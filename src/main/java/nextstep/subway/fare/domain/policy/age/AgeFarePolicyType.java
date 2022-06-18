package nextstep.subway.fare.domain.policy.age;

import java.util.Arrays;
import nextstep.subway.fare.domain.policy.age.impl.AdultAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.ChildAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.FreeAgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.impl.TeenagerAgeFarePolicy;

public enum AgeFarePolicyType {

    FREE(FreeAgeFarePolicy.getInstance()),
    ADULT(AdultAgeFarePolicy.getInstance()),
    CHILD(ChildAgeFarePolicy.getInstance()),
    TEENAGER(TeenagerAgeFarePolicy.getInstance());

    public static final int DEFAULT_FARE = 1250;
    public static final int AGE_DISCOUNT_FARE = 350;

    private final AgeFarePolicy ageFarePolicy;

    AgeFarePolicyType(AgeFarePolicy ageFarePolicy) {
        this.ageFarePolicy = ageFarePolicy;
    }

    public static AgeFarePolicyType getAgeFarePolicyType(int age) {
        return Arrays.stream(values())
            .filter(type -> type.ageFarePolicy.includeAge(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("나이에 대한 요금정책이 존재하지 않습니다."));
    }

    public boolean isFreeAgeFare() {
        return this.ageFarePolicy.equals(FreeAgeFarePolicy.getInstance());
    }

    public AgeFarePolicy getPolicy() {
        return ageFarePolicy;
    }

}

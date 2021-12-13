package nextstep.subway.member.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum MemberAgeType {
    NONE(age -> age < 6 || age >= 19),
    KID(age -> age >= 6 && age < 13),
    ADOLESCENT(age -> age >= 13 && age < 19);

    private final Function<Integer, Boolean> expression;

    MemberAgeType(Function<Integer, Boolean> expression) {
        this.expression = expression;
    }

    private boolean check(int age) {
        return expression.apply(age);
    }

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isKid() {
        return this == KID;
    }

    public static MemberAgeType getMemberAgeType(int age) {
        return Arrays.stream(values())
            .filter(ageType -> ageType.check(age))
            .findFirst()
            .orElse(NONE);
    }
}

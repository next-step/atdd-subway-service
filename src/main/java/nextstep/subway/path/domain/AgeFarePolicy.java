package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.util.Arrays;

public enum AgeFarePolicy {
    CHILD {
        @Override
        public boolean condition(LoginMember member) {
            return member.child();
        }

        @Override
        public int discountFare(int defaultFare) {
            return (int) ((defaultFare - EXCLUDE_DISCOUN_FARE) * CHILD_RATE);
        }
    },
    YOUTH {
        @Override
        public boolean condition(LoginMember member) {
            return member.youth();
        }

        @Override
        public int discountFare(int defaultFare) {
            return (int) ((defaultFare - EXCLUDE_DISCOUN_FARE) * YOUTH_RATE);
        }
    },
    DEFAULT;

    public static final double YOUTH_RATE = 0.8;
    public static final double CHILD_RATE = 0.5;
    public static final int EXCLUDE_DISCOUN_FARE = 350;

    public int discountFare(int defaultFare) {
        return defaultFare;
    }

    public boolean condition(LoginMember member) {
        return false;
    }

    public static AgeFarePolicy getPolicy(LoginMember member) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.condition(member))
                .findFirst()
                .orElse(DEFAULT);
    }
}

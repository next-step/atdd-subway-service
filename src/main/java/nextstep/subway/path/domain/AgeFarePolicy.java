package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.apache.commons.lang3.Range;

import java.util.Arrays;

public enum AgeFarePolicy {
    CHILD {
        @Override
        public boolean condition(LoginMember member) {
            Range<Integer> range = Range.between(CHILD_MIN_AGE, CHILD_MAX_AGE);
            return range.contains(member.getAge());
        }

        @Override
        public int discountFare(int defaultFare) {
            return (int) ((defaultFare - EXCLUDE_DISCOUN_FARE) * CHILD_RATE);
        }
    },
    YOUTH {
        @Override
        public boolean condition(LoginMember member) {
            Range<Integer> range = Range.between(YOUTH_MIN_AGE, YOUTH_MAX_AGE);
            return range.contains(member.getAge());
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
    private static final int YOUTH_MAX_AGE = 18;
    private static final int YOUTH_MIN_AGE = 13;
    private static final int CHILD_MAX_AGE = 12;
    private static final int CHILD_MIN_AGE = 6;

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

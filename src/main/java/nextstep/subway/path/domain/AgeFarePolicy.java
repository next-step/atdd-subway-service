package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.GuestMember;

public enum AgeFarePolicy {
    NOT_ADULT_DEDUCTION(350),

    TEENAGER_MAX_AGE(18),
    TEENAGER_MIN_AGE(13),
    TEENAGER_DISCOUNT_RATE(20),

    KID_MIN_AGE(6),
    KID_MAX_AGE(12),
    KID_DISCOUNT_RATE(50),
    ;

    private int value;

    AgeFarePolicy(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static boolean isTeenager(final AuthMember member) {
        if (member instanceof GuestMember) {
            return false;
        }
        return member.getAge() >= TEENAGER_MIN_AGE.value() && member.getAge() <= TEENAGER_MAX_AGE.value();
    }

    public static boolean isKid(final AuthMember member) {
        if (member instanceof GuestMember) {
            return false;
        }
        return member.getAge() >= KID_MIN_AGE.value() && member.getAge() <= KID_MAX_AGE.value();
    }
}

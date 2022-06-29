package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeFarePolicy;

public class GuestMember implements AuthMember {
    private final int age = AgeFarePolicy.TEENAGER_MAX_AGE.value() + 1;

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean isTeenager() {
        return false;
    }

    @Override
    public boolean isKid() {
        return false;
    }
}

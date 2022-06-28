package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AgeFarePolicy;

public class GuestMember implements AuthMember {
    private final Integer age = AgeFarePolicy.TEENAGER_MAX_AGE.value() + 1;

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean isTeenager() {
        return age >= AgeFarePolicy.TEENAGER_MIN_AGE.value() && age <= AgeFarePolicy.TEENAGER_MAX_AGE.value();
    }

    @Override
    public boolean isKid() {
        return age >= AgeFarePolicy.KID_MIN_AGE.value() && age <= AgeFarePolicy.KID_MAX_AGE.value();
    }
}

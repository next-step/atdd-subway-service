package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.FareSalePolicy;

public class GuestMember extends LoginMember{

    private Integer age;

    public GuestMember() {
        this.age = FareSalePolicy.ADULT.getOver() + 1;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}

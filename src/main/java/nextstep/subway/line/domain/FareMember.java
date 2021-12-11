package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareMember implements FarePolicy {

    private LoginMember loginMember;

    public FareMember(LoginMember loginMember) {
        this.loginMember = loginMember;
    }

    @Override
    public Fare calculateFare(Distance distance) {
        Fare distanceFare = FareRuleDistance.calculate(distance);
        Fare ageFare = FareRuleAge.calculate(loginMember.getAge());
        return Fare.sum(distanceFare, ageFare);
    }
}

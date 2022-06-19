package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.dto.Fare;
import org.springframework.stereotype.Component;

public interface FareDiscounter {
    long DEDUCTION = 350;

    boolean canDiscount(LoginMember loginMember);

    Fare discount(Fare fare);
}


@Component
class ChildrenFareDiscounter implements FareDiscounter {
    @Override
    public boolean canDiscount(LoginMember loginMember) {
        return loginMember.isChildren();
    }

    @Override
    public Fare discount(Fare fare) {
        return new Fare((long) ((fare.getWon() - DEDUCTION) * 0.5));
    }
}


@Component
class YouthFareDiscounter implements FareDiscounter {
    @Override
    public boolean canDiscount(LoginMember loginMember) {
        return loginMember.isYouth();
    }

    @Override
    public Fare discount(Fare fare) {
        return new Fare((long) (((fare.getWon()) - DEDUCTION) * 0.8));
    }
}

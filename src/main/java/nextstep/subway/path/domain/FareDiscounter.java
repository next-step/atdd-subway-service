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
    private static final double RATIO = 0.5;

    @Override
    public boolean canDiscount(LoginMember loginMember) {
        return loginMember.isChildren();
    }

    @Override
    public Fare discount(Fare fare) {
        return new Fare((long) ((fare.getWon() - DEDUCTION) * RATIO));
    }
}


@Component
class YouthFareDiscounter implements FareDiscounter {
    private static final double RATIO = 0.8;

    @Override
    public boolean canDiscount(LoginMember loginMember) {
        return loginMember.isYouth();
    }

    @Override
    public Fare discount(Fare fare) {
        return new Fare((long) (((fare.getWon()) - DEDUCTION) * RATIO));
    }
}

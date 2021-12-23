package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

public class Fare {

    private DistanceFare distanceFare;
    private AgeFare ageFare;

    Fare() {
    }

    Fare(DistanceFare distanceFare, AgeFare ageFare) {
        this.distanceFare = distanceFare;
        this.ageFare = ageFare;
    }

    public static Fare create(Route route, User user) {
        return new Fare(new DistanceFare(route), new AgeFare(user));
    }

    public Amount calculateAmount() {
        Amount amount = distanceFare.calculateAmount();
        amount.minus(ageFare.calculateDiscount(amount));
        return amount;
    }
}

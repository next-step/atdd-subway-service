package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

import java.util.Objects;

public class Fare {

    public static final int DEFAULT_AMOUNT = 1250;
    private int amount;

    protected Fare() {
    }

    public Fare(final int amount) {
        this.amount = amount;
    }

    public static Fare calculate(final Route route, User user) {
        int fare = DEFAULT_AMOUNT;

        fare += ExtraDistanceFare.calculateExtraDistanceFare(route.getDistance().getDistance());
        fare += LineFare.calculateLineFare(route.getSections());
        fare = AgeFare.calculateAgeFare(fare, user);

        return new Fare(fare);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}

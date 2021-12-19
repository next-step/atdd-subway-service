package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.LoginUser;
import nextstep.subway.domain.auth.domain.User;
import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;

import java.util.List;
import java.util.Objects;

public class Fare {

    public static final int DEFAULT_AMOUNT = 1250;
    public static final int DEFAULT_AMOUNT_DISTANCE = 10;
    private int amount;

    protected Fare() {
    }

    public Fare(final int amount) {
        this.amount = amount;
    }

    public static Fare calculate(final Distance distance, List<Line> lines, User user) {
        int fare = DEFAULT_AMOUNT;
        final int dt = distance.getDistance();
        if (isExtraDistance(dt)) {
            fare += ExtraDistanceFare.extraAmount(dt);
        }
        fare += LineFare.calculateLineFare(lines);

        if (isLoginUser(user)) {
            fare = AgeFare.calculateAgeFare(fare, user);
        }

        return new Fare(fare);
    }

    private static boolean isExtraDistance(int distance) {
        return distance > DEFAULT_AMOUNT_DISTANCE;
    }

    public int getAmount() {
        return amount;
    }

    private static boolean isLoginUser(final User user) {
        return user instanceof LoginUser;
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

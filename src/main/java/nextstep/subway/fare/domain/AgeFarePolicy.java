package nextstep.subway.fare.domain;

import java.util.function.Function;

public enum AgeFarePolicy {
    INFANT(0, fare -> new Fare(0)),
    CHILD(6, fare -> fare.minus(new Fare(350)).discount(50)),
    TEENAGER(13, fare -> fare.minus(new Fare(350)).discount(20)),
    ADULT(20, fare -> fare);

    private int startAge;
    private Function<Fare, Fare> discount;

    AgeFarePolicy(int startAge,
        Function<Fare, Fare> discount) {
        this.startAge = startAge;
        this.discount = discount;
    }

    public static AgeFarePolicy from(final int age) {
        if (age >= ADULT.startAge) {
            return ADULT;
        }

        if (age >= TEENAGER.startAge) {
            return TEENAGER;
        }

        if (age >= CHILD.startAge) {
            return CHILD;
        }

        return INFANT;
    }

    public Fare discount(Fare fare) {
        return discount.apply(fare);
    }
}

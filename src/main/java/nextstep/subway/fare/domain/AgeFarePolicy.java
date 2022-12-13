package nextstep.subway.fare.domain;

import static nextstep.subway.fare.domain.Fare.FREE;
import static nextstep.subway.fare.domain.Fare.YOUTH_DISCOUNT;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeFarePolicy {
    INFANT(0, 5, fare -> FREE),
    CHILD(6, 12, fare -> fare.minus(YOUTH_DISCOUNT).discount(50)),
    TEENAGER(13, 19, fare -> fare.minus(YOUTH_DISCOUNT).discount(20)),
    ADULT(20, Integer.MAX_VALUE, fare -> fare);

    private int startAge;
    private int endAge;
    private Function<Fare, Fare> discount;

    AgeFarePolicy(int startAge, int endAge,
        Function<Fare, Fare> discount) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.discount = discount;
    }

    public static AgeFarePolicy from(final int age) {
        return Arrays.stream(values())
            .filter(it -> it.startAge <= age && it.endAge >= age)
            .findFirst()
            .orElse(INFANT);
    }

    public Fare discount(Fare fare) {
        return discount.apply(fare);
    }
}

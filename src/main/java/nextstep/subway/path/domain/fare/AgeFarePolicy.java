package nextstep.subway.path.domain.fare;

import java.math.BigDecimal;
import java.util.Arrays;

public enum AgeFarePolicy implements FarePolicy<Fare> {
    YOUTH(13, 18, Fare.from(350), BigDecimal.valueOf(0.8)),
    CHILDREN(6, 12, Fare.from(350), BigDecimal.valueOf(0.5));

    private final int minAge;
    private final int maxAge;
    private final Fare minusFare;
    private final BigDecimal percent;

    AgeFarePolicy(int minAge, int maxAge, Fare minusFare, BigDecimal percent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minusFare = minusFare;
        this.percent = percent;
    }

    @Override
    public Fare calculateFare(Fare fare) {
        return fare.minus(minusFare)
            .multiply(percent);
    }

    public static FarePolicy<Fare> findPolicy(int age) {
        return Arrays.stream(values())
            .filter(ageFarePolicy -> ageFarePolicy.minAge <= age && age <= ageFarePolicy.maxAge)
            .map(ageFarePolicy -> (FarePolicy<Fare>)ageFarePolicy)
            .findAny()
            .orElse(fare -> fare);
    }
}

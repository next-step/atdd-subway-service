package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Age;
import nextstep.subway.line.domain.Charge;

import java.util.Arrays;
import java.util.Objects;

public enum UserCost {
    ADULT(18,0,0), TEENAGER(12,350,20), CHILD(5,350,50), GUEST(Integer.MAX_VALUE,0,0);

    private final Age minAge;
    private final Charge deduction;
    private final Discount discount;

    UserCost(int minAge, final long deduction, final long discount) {
        this.minAge = new Age(minAge);
        this.deduction = new Charge(deduction);
        this.discount = new Discount(discount);
    }

    public Charge calculate(final Charge payableCharge) {
        return discount.calculate(payableCharge.minus(deduction));
    }

    public static UserCost valueOf(final Age age) {
        return Arrays.stream(UserCost.values())
                .filter(it -> it != GUEST)
                .filter(it -> age.isHigh(it.minAge))
                .findFirst()
                .orElse(GUEST);
    }
}

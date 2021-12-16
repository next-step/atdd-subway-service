package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Money;

import java.util.Objects;

public class Fare implements FareRule {
    private static final int BASE_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final Money money;

    public Fare() {
        this.money = new Money(BASE_FARE);
    }

    public Fare(Money money) {
        this.money = money;
    }

    public Fare(int money) {
        this.money = new Money(money);
    }

    @Override
    public Fare extraFare(int distance, int lineFare) {
        int overDistance = ExtraDistance.getOverDistance(distance);
        Money extraDistanceFare = money.plus(overDistance * EXTRA_FARE);
        Money totalExtraFare = extraDistanceFare.plus(lineFare);
        return new Fare(totalExtraFare);
    }

    @Override
    public Fare discount(int age) {
        DiscountAge discountAge = DiscountAge.findBy(age);
        Money deductionFare = money.minus(discountAge.getDeduction());
        Money discountFare = deductionFare.multiply(discountAge.getRate());
        return new Fare(discountFare);
    }

    public int getMoney() {
        return money.getMoney();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return Objects.equals(money, fare.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}

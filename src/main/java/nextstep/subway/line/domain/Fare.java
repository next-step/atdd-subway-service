package nextstep.subway.line.domain;

import nextstep.subway.path.domain.overfare.DefaultOverFare;
import nextstep.subway.path.domain.overfare.OverFare;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparing;

@Embeddable
public class Fare {

    public static BigInteger DEFAULT_DISCOUNT_PRICE = new BigInteger("350");
    public static double DEFAULT_CHILD_RATE = 0.5;
    public static double DEFAULT_YOUTH_RATE = 0.8;

    private BigInteger value;

    protected Fare() {

    }

    public Fare(final int value) {
        validateNegativeFare(value);
        this.value = BigInteger.valueOf(value);
    }

    public Fare(final BigInteger value) {
        validateNegativeFare(value.intValue());
        this.value = value;
    }

    public static Fare overFareCalculate(Distance distance) {
        OverFare overFare = new DefaultOverFare();
        return new Fare(overFare.calculate(distance.getValue()));
    }

    private void validateNegativeFare(int value) {
        if(value < 0) {
            throw new IllegalArgumentException("0보다 작을 수 없습니다.");
        }
    }

    public BigInteger getValue() {
        return value;
    }

    public long getValueAsLong() {
        return value.longValue();
    }

    public Fare plusLineUseFare(List<Line> lines) {
        BigInteger lineUseFare = lines.stream()
                .map(Line::getFare)
                .max(comparing(Fare::getValue))
                .map(Fare::getValue)
                .orElse(BigInteger.ZERO);

        return new Fare(this.value.add(lineUseFare));
    }

    public Fare minus(BigInteger subtractValue) {
        BigInteger subtract = this.value.subtract(subtractValue);
        return new Fare(subtract);
    }

    public Fare multiply(double multiplyValue) {
        double multiply = this.value.longValue() * multiplyValue;
        return new Fare((int) multiply);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

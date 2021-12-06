package nextstep.subway.policy.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price")
    private int value;

    protected Price() {
        value = 0;
    }

    private Price (int value) {
        this.value = value;
    }

    public static Price of(int value) {
        return new Price(value);
    }

    public Price plus(Price price) {
        return new Price(value + price.value);
    }

    public int value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Price)) {
            return false;
        }
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

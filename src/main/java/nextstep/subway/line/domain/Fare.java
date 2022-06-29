package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare implements Comparable<Fare>{

    @Column
    private int value;

    protected Fare() {
    }

    public Fare(int value) {
        this.value = value;
    }

    public void plus(Fare fare) {
        this.value += fare.value;
    }

    public void minus(Fare fare) {
        this.value -= fare.value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Fare fare) {
        return Integer.compare(value, fare.value);
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

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}

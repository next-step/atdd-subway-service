package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Charge {
    private static final int BASIC_CHARGE = 1_250;

    @Column(name = "charge", nullable = false)
    private final int value;

    protected Charge() {
        this.value = BASIC_CHARGE;
    }

    public Charge(final int value) {
        validation(value);
        this.value = value;
    }

    public static Charge ofBasic() {
        return new Charge(BASIC_CHARGE);
    }

    public static Charge ofExtraCharge(final int value) {
        return new Charge(BASIC_CHARGE + value);
    }

    private void validation(final int value) {
        if (value < BASIC_CHARGE) {
            throw new LineException(LineExceptionType.LESS_BASIC_CHARGE);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Charge charge = (Charge) o;
        return value == charge.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


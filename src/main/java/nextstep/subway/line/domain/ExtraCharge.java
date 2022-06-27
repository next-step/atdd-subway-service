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
public class ExtraCharge {
    public static final int DEFAULT_EXTRA_CHARGE = 0;

    @Column(name = "extra_charge", nullable = false)
    private final int value;

    protected ExtraCharge() {
        this.value = DEFAULT_EXTRA_CHARGE;
    }

    public ExtraCharge(final int value) {
        validation(value);
        this.value = value;
    }

    public static ExtraCharge of(final int value) {
        return new ExtraCharge(value);
    }

    private void validation(final int value) {
        if (value < DEFAULT_EXTRA_CHARGE) {
            throw new LineException(LineExceptionType.MIN_DEFAULT_EXTRA_CHARGE);
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
        final ExtraCharge charge = (ExtraCharge) o;
        return value == charge.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.exception.InvalidChargeException;

@Embeddable
public class ExtraCharge {
    @Column
    private int extraCharge;

    protected ExtraCharge() {
    }

    private ExtraCharge(int extraCharge) {
        validate(extraCharge);
        this.extraCharge = extraCharge;
    }

    private void validate(int extraCharge) {
        if (extraCharge < 0) {
            throw new InvalidChargeException();
        }
    }

    public static ExtraCharge of(int extraCharge) {
        return new ExtraCharge(extraCharge);
    }

    public static ExtraCharge max(ExtraCharge first, ExtraCharge second) {
        int maxCharge = Math.max(first.extraCharge, second.extraCharge);
        return new ExtraCharge(maxCharge);
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraCharge that = (ExtraCharge) o;
        return extraCharge == that.extraCharge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraCharge);
    }
}

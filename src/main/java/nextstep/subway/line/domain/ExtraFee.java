package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidExtraFeeException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ExtraFee {
    @Transient
    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

    private BigDecimal extraFee;

    protected ExtraFee() {
    }

    private ExtraFee(BigDecimal extraFee) {
        validate(extraFee);
        this.extraFee = extraFee;
    }

    public static ExtraFee of(BigDecimal extraFee) {
        if (extraFee == null) {
            return new ExtraFee(MIN_VALUE);
        }
        return new ExtraFee(extraFee);
    }

    public void validate(BigDecimal extraFee) {
        if (extraFee.compareTo(MIN_VALUE) < 0) {
            throw new InvalidExtraFeeException("환승 추가금은 0원 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ExtraFee extraFee1 = (ExtraFee) o;
        return Objects.equals(extraFee, extraFee1.extraFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraFee);
    }
}

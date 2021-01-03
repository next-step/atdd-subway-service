package nextstep.subway.path.domain.fee.transferFee;

import java.math.BigDecimal;
import java.util.Objects;

public class LineWithExtraFee {
    private final Long lineId;
    private final BigDecimal transferExtraFee;

    public LineWithExtraFee(final Long lineId, final BigDecimal transferExtraFee) {
        this.lineId = lineId;
        this.transferExtraFee = transferExtraFee;
    }

    public BigDecimal getTransferExtraFee() {
        return this.transferExtraFee;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineWithExtraFee that = (LineWithExtraFee) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(transferExtraFee, that.transferExtraFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, transferExtraFee);
    }

    @Override
    public String toString() {
        return "LineWithExtraFee{" +
                "lineId=" + lineId +
                ", transferExtraFee=" + transferExtraFee +
                '}';
    }
}

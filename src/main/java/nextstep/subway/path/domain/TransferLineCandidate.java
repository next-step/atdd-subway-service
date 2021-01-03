package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferLineCandidate {
    private final Long lineId;
    private final BigDecimal transferExtraFee;

    public TransferLineCandidate(final Long lineId, final BigDecimal transferExtraFee) {
        this.lineId = lineId;
        this.transferExtraFee = transferExtraFee;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TransferLineCandidate that = (TransferLineCandidate) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(transferExtraFee, that.transferExtraFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, transferExtraFee);
    }
}

package nextstep.subway.path.domain.fee.transferFee;

import nextstep.subway.path.domain.exceptions.CannotFindTransferFeeException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TransferLines {
    private static final int NO_TRANSFER_BOUNDARY = 0;

    private final List<LineWithExtraFee> transferLines;

    public TransferLines(final List<LineWithExtraFee> transferLines) {
        this.transferLines = transferLines;
    }

    public BigDecimal calculateTransferFee() {
        if (isNotTransferred()) {
            return BigDecimal.ZERO;
        }
        return this.transferLines.stream()
                .max(Comparator.comparing(LineWithExtraFee::getTransferExtraFee))
                .map(LineWithExtraFee::getTransferExtraFee)
                .orElseThrow(() -> new CannotFindTransferFeeException("환승비를 찾을 수 없습니다."));
    }

    private boolean isNotTransferred() {
        return this.transferLines.size() == NO_TRANSFER_BOUNDARY;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TransferLines that = (TransferLines) o;
        return this.transferLines.equals(that.transferLines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferLines);
    }
}

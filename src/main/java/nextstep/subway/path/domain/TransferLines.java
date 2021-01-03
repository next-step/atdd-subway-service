package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

public class TransferLines {
    private final List<LineWithExtraFee> transferLines;

    public TransferLines(final List<LineWithExtraFee> transferLines) {
        this.transferLines = transferLines;
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

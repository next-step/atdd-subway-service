package nextstep.subway.path.domain.fee.transferFee;

import java.util.*;
import java.util.stream.Collectors;

public class LineOfStationInPath {
    private static final int MULTI_LINE_BOUNDARY = 1;

    private final List<LineWithExtraFee> lineWithExtraFees;

    public LineOfStationInPath(List<LineWithExtraFee> lineWithExtraFees) {
        this.lineWithExtraFees = new ArrayList<>(lineWithExtraFees);
    }

    public boolean isMultiLine() {
        return this.lineWithExtraFees.size() > MULTI_LINE_BOUNDARY;
    }

    public TransferCandidates findTransferCandidates(LineOfStationInPath that) {
        List<LineWithExtraFee> transferCandidates = that.lineWithExtraFees.stream()
                .filter(this.lineWithExtraFees::contains)
                .collect(Collectors.toList());

        return new TransferCandidates(transferCandidates);
    }

    public TransferCandidates findFirstTransferCandidate() {
        return new TransferCandidates(Collections.singletonList(lineWithExtraFees.get(0)));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineOfStationInPath that = (LineOfStationInPath) o;
        return this.lineWithExtraFees.equals(that.lineWithExtraFees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineWithExtraFees);
    }
}

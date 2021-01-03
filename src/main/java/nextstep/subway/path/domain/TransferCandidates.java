package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.CannotFindMinExtraFeeLineException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TransferCandidates {
    private static final Integer MIN_SIZE = 1;

    private final List<LineWithExtraFee> transferCandidates;

    public TransferCandidates(List<LineWithExtraFee> transferCandidates) {
        this.transferCandidates = transferCandidates;
    }

    public boolean isValidCandidate() {
        return (this.transferCandidates.size() >= MIN_SIZE);
    }

    public LineWithExtraFee confirmTransferLine() {
        return this.transferCandidates.stream()
                .min(Comparator.comparing(LineWithExtraFee::getTransferExtraFee))
                .orElseThrow(() -> new CannotFindMinExtraFeeLineException("최소 금액을 찾을 수 없습니다."));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TransferCandidates that = (TransferCandidates) o;
        return this.transferCandidates.equals(that.transferCandidates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferCandidates);
    }
}

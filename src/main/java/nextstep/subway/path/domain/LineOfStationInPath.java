package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.CannotFindMinExtraFeeLineException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

//    public LineWithExtraFee findTransferLine(LineOfStationInPath that) {
//        return getSameLines(that).stream()
//                .min(Comparator.comparing(LineWithExtraFee::getTransferExtraFee))
//                .orElseThrow(() -> new CannotFindMinExtraFeeLineException("환승비가 제일 적은 노선을 찾을 수 없습니다."));
//    }

    public List<LineWithExtraFee> findTransferCandidates(LineOfStationInPath that) {
        return that.lineWithExtraFees.stream()
                .filter(this.lineWithExtraFees::contains)
                .collect(Collectors.toList());
    }

    public LineWithExtraFee findMinOfExtraFee() {
        return lineWithExtraFees.stream()
                .min(Comparator.comparing(LineWithExtraFee::getTransferExtraFee))
                .orElseThrow(() -> new CannotFindMinExtraFeeLineException("환승비가 제일 적은 노선을 찾을 수 없습니다."));
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

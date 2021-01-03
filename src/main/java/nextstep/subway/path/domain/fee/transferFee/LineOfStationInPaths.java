package nextstep.subway.path.domain.fee.transferFee;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LineOfStationInPaths {
    private static final Integer TRANSFER_BOUNDARY = 0;

    private final List<LineOfStationInPath> lineOfStationInPaths;

    public LineOfStationInPaths(List<LineOfStationInPath> lineOfStationInPaths) {
        this.lineOfStationInPaths = lineOfStationInPaths;
    }

    public TransferLines findTransferLines() {
        if (isNotTransferred()) {
            return TransferLines.of(new ArrayList<>(), isLastMulti());
        }

        List<LineWithExtraFee> transferLines = this.findTransferCandidatesOfPath().stream()
                .map(TransferCandidates::confirmTransferLine)
                .collect(Collectors.toList());

        return TransferLines.of(transferLines, isLastMulti());
    }

    List<TransferCandidates> findTransferCandidatesOfPath() {
        List<LineOfStationInPath> multiLines = this.findMultiLines();

        return multiLines.stream().map(it -> {
            LineOfStationInPath next = findNext(it);
            return it.findTransferCandidates(next);
        }).filter(TransferCandidates::isValidCandidate).collect(Collectors.toList());
    }

    LineOfStationInPath findNext(LineOfStationInPath lineOfStationInPath) {
        int targetIndex = this.indexOf(lineOfStationInPath) + 1;

        if (targetIndex == this.size()) {
            return new LineOfStationInPath(new ArrayList<>());
        }

        return this.get(targetIndex);
    }

    List<LineOfStationInPath> findMultiLines() {
        return this.lineOfStationInPaths.stream()
                .filter(LineOfStationInPath::isMultiLine)
                .collect(Collectors.toList());
    }

    private LineOfStationInPath get(int index) {
        return this.lineOfStationInPaths.get(index);
    }

    private int size() {
        return this.lineOfStationInPaths.size();
    }

    private int indexOf(LineOfStationInPath lineOfStationInPath) {
        return this.lineOfStationInPaths.indexOf(lineOfStationInPath);
    }

    private boolean isNotTransferred() {
        return this.findMultiLines().size() == TRANSFER_BOUNDARY;
    }

    private boolean isLastMulti() {
        return lineOfStationInPaths.get(size() - 1).isMultiLine();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineOfStationInPaths that = (LineOfStationInPaths) o;
        return this.lineOfStationInPaths.equals(that.lineOfStationInPaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineOfStationInPaths);
    }
}

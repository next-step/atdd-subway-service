package nextstep.subway.path.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineOfStationInPaths {
    private final List<LineOfStationInPath> lineOfStationInPaths;

    public LineOfStationInPaths(List<LineOfStationInPath> lineOfStationInPaths) {
        this.lineOfStationInPaths = lineOfStationInPaths;
    }

    public List<List<LineWithExtraFee>> findTransferLine() {
        List<LineOfStationInPath> multiLines = this.findMultiLines();

        return multiLines.stream().map(it -> {
            LineOfStationInPath next = findNext(it);
            return next.findTransferCandidates(it);
        }).collect(Collectors.toList());
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
}

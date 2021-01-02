package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineOfStationInPaths {
    private final List<LineOfStationInPath> lineOfStationInPaths;

    public LineOfStationInPaths(final List<LineOfStationInPath> lineOfStationInPaths) {
        this.lineOfStationInPaths = lineOfStationInPaths;
    }

    LineOfStationInPath findNext(final LineOfStationInPath lineOfStationInPath) {
        int index = this.lineOfStationInPaths.indexOf(lineOfStationInPath);
        int targetIndex = index + 1;

        if (targetIndex == this.lineOfStationInPaths.size()) {
            return new LineOfStationInPath(new ArrayList<>());
        }

        return this.lineOfStationInPaths.get(targetIndex);
    }

    List<LineOfStationInPath> findMultiLines() {
        return this.lineOfStationInPaths.stream()
                .filter(LineOfStationInPath::isMultiLine)
                .collect(Collectors.toList());
    }
}

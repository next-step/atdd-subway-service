package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineOfStationInPaths {
    private final List<LineOfStationInPath> lineOfStationInPaths;

    public LineOfStationInPaths(List<LineOfStationInPath> lineOfStationInPaths) {
        this.lineOfStationInPaths = lineOfStationInPaths;
    }

    LineOfStationInPath findNext(LineOfStationInPath lineOfStationInPath) {
        int targetIndex = indexOf(lineOfStationInPath) + 1;

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

    private int indexOf(LineOfStationInPath lineOfStationInPath) {
        return this.lineOfStationInPaths.indexOf(lineOfStationInPath);
    }
}

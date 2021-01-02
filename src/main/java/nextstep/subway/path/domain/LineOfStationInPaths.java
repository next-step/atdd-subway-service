package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

public class LineOfStationInPaths {
    private final List<LineOfStationInPath> lineOfStationInPaths;

    public LineOfStationInPaths(final List<LineOfStationInPath> lineOfStationInPaths) {
        this.lineOfStationInPaths = lineOfStationInPaths;
    }

    public List<LineOfStationInPath> findMultiLines() {
        return this.lineOfStationInPaths.stream()
                .filter(LineOfStationInPath::isMultiLine)
                .collect(Collectors.toList());
    }
}

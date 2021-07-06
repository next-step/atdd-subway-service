package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.path.domain.PathGraph;
import nextstep.subway.path.domain.PathGraphCreator;

public class Lines implements PathGraphCreator {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public PathGraph createPathGraph() {
        PathGraph pathGraph = new PathGraph();
        lines.forEach(line -> line.addPathInfoTo(pathGraph));
        return pathGraph;
    }

    public int getFinalSurcharge() {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }
}

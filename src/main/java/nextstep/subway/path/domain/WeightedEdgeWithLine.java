package nextstep.subway.path.domain;

import org.jgrapht.graph.*;

import nextstep.subway.line.domain.*;

public class WeightedEdgeWithLine extends DefaultWeightedEdge {
    private final Line line;

    private WeightedEdgeWithLine(Line line) {
        this.line = line;
    }

    public static WeightedEdgeWithLine from(Line line) {
        return new WeightedEdgeWithLine(line);
    }

    public Line line() {
        return line;
    }
}

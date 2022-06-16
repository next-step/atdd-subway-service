package nextstep.subway.line.utils.PathFinder;

import nextstep.subway.line.domain.Line;
import org.jgrapht.graph.DefaultWeightedEdge;

public class DijkstraWeightedEdgeWithLine extends DefaultWeightedEdge {

    private Line line;

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}

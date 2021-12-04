package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.jgrapht.graph.DefaultWeightedEdge;

public class LineWeightedEdge extends DefaultWeightedEdge {

    private Line line;

    public LineWeightedEdge() {

    }
    public LineWeightedEdge(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }


    @Override
    public String toString() {
        return "LineWeightedEdge{" +
                "line=" + line +
                '}';
    }

}

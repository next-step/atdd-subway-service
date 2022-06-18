package nextstep.subway.line.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Lines {
    List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void registerPath(SectionGraph graph) {
        for (Line line : lines) {
            line.registerPath(graph);
        }
    }

    public DijkstraShortestPath createPath(SectionGraph graph) {
        return graph.getPath(this);
    }

    public List<Line> getLines() {
        return lines;
    }
}

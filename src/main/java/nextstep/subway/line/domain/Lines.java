package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Lines {
    List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public DijkstraShortestPath createPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            line.registerPath(graph);
        }

        return new DijkstraShortestPath(graph);
    }
}

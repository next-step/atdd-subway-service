package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {

    List<Line> lines = new ArrayList<>();

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public GraphPath minPath(Station sourceStation, Station targetStation) {

        validatePath(sourceStation, targetStation);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            line.addPathInfo(graph);
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private void validatePath(Station sourceStation, Station targetStation){
        if(sourceStation.equals(targetStation)){
            throw new IllegalArgumentException("경로 시작역과 도착역이 일치합니다.");
        }
    }
}

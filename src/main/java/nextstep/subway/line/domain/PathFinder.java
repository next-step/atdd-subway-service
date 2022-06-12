package nextstep.subway.line.domain;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import nextstep.subway.exception.PathFindException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
        DefaultWeightedEdge.class);
    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        for (Line line : lines) {
            setVertexPerLine(line);
            setWeightPerLine(line);
        }

        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void setVertexPerLine(Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void setWeightPerLine(Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getDownStation(), section.getUpStation()),
                section.getDistance().getDistance());
        }
    }

    public GraphPath findPath(Station sourceStation, Station targetStation) {
        validateNoStationToFind(sourceStation, targetStation);
        validateSameStation(sourceStation, targetStation);

        GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);

        validateNoPathToTarget(path);

        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new PathFindException("출발지와 종착지가 같으면 안됩니다");
        }
    }

    private void validateNoStationToFind(Station sourceStation, Station targetStation) {
        if (isEmpty(sourceStation) || isEmpty(targetStation)) {
            throw new PathFindException("존재하지않는 정거장입니다");
        }
        if (!(graph.containsVertex(sourceStation)) || !(graph.containsVertex(targetStation))) {
            throw new PathFindException("존재하지않는 정거장입니다");
        }
    }

    private void validateNoPathToTarget(GraphPath graphPath){
        if(isEmpty(graphPath)){
            throw new PathFindException("경로를 찾을수 없습니다");
        }
    }
}

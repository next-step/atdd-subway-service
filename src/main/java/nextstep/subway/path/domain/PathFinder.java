package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);


    public PathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            setEdgeWeight(line.getSections());
        });
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(lines);
    }

    private void addVertex(Line line) {
        line.getStations().forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance()
                )
        );
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        checkEqualStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
//        checkPathExist(path);
        return Path.of(path.getVertexList(), (int) path.getWeight());
    }

    private void checkEqualStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new RuntimeException(ErrorEnum.SOURCE_AND_TARGET_EQUAL_STATION.message());
        }
    }

//    private void checkPathExist(GraphPath<Station, DefaultWeightedEdge> path) {
//        if(path == null){
//            throw new RuntimeException(ErrorEnum.NOT_CONNECTED_STATIONS.message());
//        }
//    }
}

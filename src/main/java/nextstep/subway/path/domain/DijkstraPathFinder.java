package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

    private DijkstraPathFinder(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            setEdgeWeight(line.getSections());
        });
    }

    public static DijkstraPathFinder createGraph(List<Line> lines) {
        return new DijkstraPathFinder(lines);
    }

    private void addVertex(Line line) {
        line.findStations().forEach(graph::addVertex);
    }

    private void setEdgeWeight(Sections sections) {
        sections.getSections().forEach(
                section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.distanceValue()));
    }

    @Override
    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateEqualStations(sourceStation, targetStation);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        List<Station> shortestPathVertexes = shortestPath.getVertexList();
        double shortestPathWeight = shortestPath.getWeight();

        return Path.of(shortestPathVertexes, shortestPathWeight);
    }

    private void validateEqualStations(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorCode.출발역과_도착역이_서로_같음.getErrorMessage());
        }
    }
}

package nextstep.subway.path.application;

import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.path.application.CommonMessage.MESSAGE_STATIONS_NOT_ABLE_TO_REACHED;

public class DijkstraPathFinder implements PathFindAlgorithm {
    @Override
    public List<Station> findShortestPathStations(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation) {
        GraphPath<Station, SectionEdge> shortestPathGraph = getShortestPathGraph(graph, departStation, destStation);
        if (Objects.isNull(shortestPathGraph)) {
            throw new IllegalArgumentException(MESSAGE_STATIONS_NOT_ABLE_TO_REACHED);
        }
        return shortestPathGraph.getVertexList();
    }

    @Override
    public GraphPath<Station, SectionEdge> getShortestPathGraph(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(departStation, destStation);
    }
}

package nextstep.subway.path.application;

import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static nextstep.subway.path.application.CommonMessage.MESSAGE_STATIONS_NOT_ABLE_TO_REACHED;

public class KShortestPathFinder implements PathFindAlgorithm {

    private final int defaultK = 100;

    @Override
    public List<Station> findShortestPathStations(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation) {
        GraphPath<Station, SectionEdge> shortestPathGraph = getShortestPathGraph(graph, departStation, destStation);
        return shortestPathGraph.getVertexList();
    }

    @Override
    public GraphPath<Station, SectionEdge> getShortestPathGraph(WeightedMultigraph<Station, SectionEdge> graph, Station departStation, Station destStation) {
        List<GraphPath> paths = new KShortestPaths(graph, defaultK).getPaths(departStation, destStation);
        if (CollectionUtils.isEmpty(paths)) {
            throw new IllegalArgumentException(MESSAGE_STATIONS_NOT_ABLE_TO_REACHED);
        }
        return paths.get(0);
    }
}

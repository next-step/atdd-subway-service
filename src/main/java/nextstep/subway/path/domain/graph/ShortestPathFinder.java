package nextstep.subway.path.domain.graph;

import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public interface ShortestPathFinder {
    ShortestPathFinder addGraph(Graph<Station, DefaultWeightedEdge> stationGraph);
    List<Station> getShortestPath(Station source, Station target);
    int getDistance(Station source, Station target);
    boolean isNotConnectStations(Station source, Station target);
}

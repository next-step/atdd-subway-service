package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;

public interface PathFinderGraph {
    void addVertices(List<Station> stations);

    void addEdgeAndWeight(List<Section> sections);

    DijkstraShortestPath getPath();
}

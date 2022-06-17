package nextstep.subway.line.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public interface PathFinderGraph {
    void addGraphComposition(Lines lines);

    DijkstraShortestPath getPath(Lines lines);
}

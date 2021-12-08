package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface StationGraph {

    void addVertex(Station s);

    void addEdgeWithDistance(Station source, Station target, Integer distance);

    boolean containsVertex(Station station);

    Path getShortestPath(Station source, Station target);
}

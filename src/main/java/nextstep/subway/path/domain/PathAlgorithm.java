package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathAlgorithm {
    List<Station> getShortestPath(Station source, Station target);
    int getDistance(Station source, Station target);
    Object getPath(Station source, Station target);
}

package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFinderStrategy {

    Path shortestPath(Station source, Station target);

}

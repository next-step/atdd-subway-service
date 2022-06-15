package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {

    Path getPath(Station source, Station target);

}

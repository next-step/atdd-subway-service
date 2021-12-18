package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

public interface PathFinderInterface {

    boolean containsStation(Station station);

    boolean stationsConnected(Station src, Station dest);

    Path getShortestPath(Station src, Station dest);

}

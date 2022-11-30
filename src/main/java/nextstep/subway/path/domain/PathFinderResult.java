package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinderResult {

    int getDistance();

    List<Station> getStations();

}

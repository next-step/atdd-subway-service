package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {

    List<Station> findAllStationsByStations(Station 양재역, Station 서현역);
}

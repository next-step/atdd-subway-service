package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public interface ShortestPathFinder {
    List<Station> getShortestStations(Long startStationId, Long endStationId);

}

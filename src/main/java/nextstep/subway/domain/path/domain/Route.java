package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.path.exception.NotConnectedStation;
import nextstep.subway.domain.station.domain.Station;

import java.util.List;

public class Route {

    private static final int PATH_NODE_MINIMUM_NUMBER = 2;
    private List<Station> stations;
    private Distance distance;

    public Route(final List<Station> stations, final Distance distance) {
        pathNodeMinimumNumberValidation(stations);
        this.stations = stations;
        this.distance = distance;
    }

    private void pathNodeMinimumNumberValidation(List<Station> stations) {
        if (stations.size() < PATH_NODE_MINIMUM_NUMBER) {
            throw new NotConnectedStation();
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class Path {
    private double distance;

    private List<Station> stations;
    private Path(final double distance, final List<Station> stations) {
        validateConnectedStations(stations);
        this.distance = distance;
        this.stations = stations;
    }

    public static Path of(final double weight, final List<Station> stations) {
        return new Path(weight, stations);
    }

    private void validateConnectedStations(final List<Station> stations) {
        if (CollectionUtils.isEmpty(stations)) {
            throw new PathDisconnectedException();
        }
    }

    public double getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}

package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class Path {
    private static final String ERROR_MESSAGE_STATIONS_LESS_THAN_MIN_SIZE_FORMAT = "노선에는 최소 %d개의 지하철역이 포함되어야 합니다.";
    private static final int MINIMUM_STATION_SIZE = 2;

    private final Stations stations;
    private final Distance distance;

    private Path(Stations stations, Distance distance) {
        validSize(stations);
        this.stations = stations;
        this.distance = distance;
    }

    private static void validSize(Stations stations) {
        if (stations.isLessThan(MINIMUM_STATION_SIZE)) {
            throw new InvalidParameterException(String.format(ERROR_MESSAGE_STATIONS_LESS_THAN_MIN_SIZE_FORMAT, MINIMUM_STATION_SIZE));
        }
    }

    public static Path of(Stations stations, Distance distance) {
        return new Path(stations, distance);
    }

    public List<Station> stations() {
        return stations.list();
    }

    public int distanceValue() {
        return distance.value();
    }
}

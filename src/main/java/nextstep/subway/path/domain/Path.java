package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private Distance distance;

    private Path(List<Station> stations, int distance) {
        validatePathStations(stations);
        this.stations = new ArrayList<>(stations);
        this.distance = Distance.from(distance);
    }

    public static Path of(List<Station> stations, double distance) {
        return new Path(stations, (int) distance);
    }

    private void validatePathStations(List<Station> stations) {
        if(stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.경로는_비어있을_수_없음.getErrorMessage());
        }
    }

    public List<Station> unmodifiableStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}

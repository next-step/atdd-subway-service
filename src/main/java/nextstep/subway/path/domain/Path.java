package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.domain.Station;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class Path {

    private final List<Station> pathStations;

    private final int distance;

    private Path(List<Station> pathStations, int distance) {
        this.pathStations = pathStations;
        this.distance = distance;
    }

    public static Path of(List<Station> pathStations, int distance) {
        if (pathStations.isEmpty()) {
            throw new NotConnectedPathException("연결되지 않은 구간입니다.");
        }
        return new Path(pathStations, distance);
    }

    public List<Station> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }
}

package nextstep.subway.path.domain;

import java.util.List;

public class Path {

    private final List<PathStation> pathStations;

    private final Distance distance;

    public Path(final List<PathStation> pathStations, final int distance) {
        this(pathStations, Distance.valueOf(distance));
    }

    public Path(final List<PathStation> pathStations, final Distance distance) {
        this.pathStations = pathStations;
        this.distance = distance;
    }

    public List<PathStation> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance.getValue();
    }
}

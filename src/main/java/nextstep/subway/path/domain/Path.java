package nextstep.subway.path.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Path {

    private final List<PathStation> pathStations;

    private final int distance;

    public Path(final List<PathStation> pathStations, final int distance) {
        this.pathStations = pathStations;
        this.distance = distance;
    }
}

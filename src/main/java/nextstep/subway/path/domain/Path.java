package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class Path {

    private final List<Station> pathStations;

    private final int distance;

    private final Money maxAdditionalFare;

    private Path(List<Station> pathStations, int distance, Money maxAdditionalFare) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.maxAdditionalFare = maxAdditionalFare;
    }

    public static Path of(List<Station> pathStations, int distance, Money maxAdditionalFare) {
        return new Path(pathStations, distance, maxAdditionalFare);
    }

    public List<Station> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }

    public Money getMaxAdditionalFare() {
        return maxAdditionalFare;
    }
}

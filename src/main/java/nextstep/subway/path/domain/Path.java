package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;

    private final Distance distance;

    private final ExtraFare extraFare;

    private Path(List<Station> stations, int distance, int extraFare) {
        this.stations = stations;
        this.distance = Distance.from(distance);
        this.extraFare = calculateExtraFare(distance);
    }

    public static Path of(List<Station> stations, int distance, int extraFare) {
        return new Path(stations, distance, extraFare);
    }

    private ExtraFare calculateExtraFare(int distance) {
        return ExtraFare.from(ExtraFare.ZERO).add(DistanceDiscount.calculate(distance));
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public ExtraFare getExtraFare() {
        return extraFare;
    }
}

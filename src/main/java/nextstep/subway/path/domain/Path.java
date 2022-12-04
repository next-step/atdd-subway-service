package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.path.domain.age.AgeDiscount;
import nextstep.subway.path.domain.distance.DistanceDiscount;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;

    private final Distance distance;

    private final ExtraFare extraFare;

    private Path(List<Station> stations, int distance, int extraFare, int age) {
        this.stations = stations;
        this.distance = Distance.from(distance);
        this.extraFare = calculateExtraFare(extraFare, distance, age);
    }

    public static Path of(List<Station> stations, int distance, int extraFare, int age) {
        return new Path(stations, distance, extraFare, age);
    }

    private ExtraFare calculateExtraFare(int extraFare, int distance, int age) {
        ExtraFare discountExtraFare = ExtraFare.from(extraFare).add(DistanceDiscount.calculate(distance));
        return ExtraFare.from(discountExtraFare.subtract(
            AgeDiscount.calculate(age, discountExtraFare.value())).value());
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

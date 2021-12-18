package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Line> lines;
    private final List<Station> stations;
    private final int distance;

    public Path(final List<Line> lines, final List<Station> stations, final int distance) {
        this.lines = new ArrayList<>(lines);
        this.stations = new ArrayList<>(stations);
        this.distance = distance;
    }

    public Fare calculateFare(final Integer age) {
        final Fare baseFare = Fare.of();
        final Fare overFareByDistance = baseFare.calculateOverFare(new FareDistance(distance));
        final Fare overFareByLine = lines.stream()
            .map(Line::getSurcharge)
            .max(Fare::compareTo)
            .orElse(baseFare);
        final Fare total = overFareByDistance.add(overFareByLine).applyDiscount(age);
        return new Fare(BigDecimal.valueOf(total.getFare().longValue()));
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}


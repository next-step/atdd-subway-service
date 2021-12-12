package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final List<Section> sections;
    private final Long distance;
    private final Fare fare;

    public Path(List<Station> stations, List<Section> sections, Long distance) {
        this.stations = stations;
        this.sections = sections;
        this.distance = distance;
        this.fare = Fare.from(distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Long getDistance() {
        return distance;
    }

    public BigInteger getFare() {
        return fare.getFare();
    }
}

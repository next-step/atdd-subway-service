package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Long distance;
    private Fare fare;

    public Path(List<Station> stations, List<Section> sections, Long distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = Fare.of(distance, sections);
    }

    public void disCountByAge(int age){
        this.fare = this.fare.calculateByAgeFare(age);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Long getDistance() {
        return distance;
    }

    public Long getFare() {
        return fare.getFare();
    }
}

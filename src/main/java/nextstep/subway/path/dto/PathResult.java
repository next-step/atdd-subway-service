package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResult {

    private final List<Station> stations;
    private final Sections sections;
    private final int distance;
    private Fare fare;

    public PathResult(List<Station> stations, double distance, Sections sections) {
        this.stations = stations;
        this.sections = sections;
        this.distance = (int) distance;
    }

    public void calculateFare(int age) {
        this.fare = Fare.extra(this.sections.getMaxLineFare(), this.distance, age);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getValue();
    }
}

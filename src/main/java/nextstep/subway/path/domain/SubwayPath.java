package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private List<Station> stations;
    private int distance;
    private Fare fare;

    public SubwayPath() {
    }

    public SubwayPath(List<Station> stations, List<Section> sections, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.of(distance, sections);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }

    public void discountByAge(int age) {
        this.fare = this.fare.calculateByAge(age);
    }
}

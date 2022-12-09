package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private Price price;
    private List<SectionEdge> sections;

    public Path(List<Station> stations, Distance distance, List<SectionEdge> sections) {
        this.stations = stations;
        this.distance = distance;
        this.sections = sections;
    }

    public void calculatePrice(int age) {
        this.price = new Price();
        price.calculatePrice(distance.value(), sections, age);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.value();
    }

    public int getPrice() {
        return price.value();
    }

}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.SectionDistance;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private ShortestDistance distance;
    private List<SectionDistance> sectionDistances;

    public Path(List<Station> stations, ShortestDistance distance, List<SectionDistance> sectionDistances) {
        this.stations = stations;
        this.distance = distance;
        this.sectionDistances = sectionDistances;
    }

    public List<Station> getStations() {
        return stations;
    }

    public ShortestDistance getDistance() {
        return distance;
    }

    public List<SectionDistance> getSectionDistances() {
        return sectionDistances;
    }
}

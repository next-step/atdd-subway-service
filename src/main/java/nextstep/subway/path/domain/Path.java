package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class Path {
    private List<Station> stations;
    private Set<Line> lines;
    private Integer distance;

    public Path() {
    }

    public Path(Set<Line> lines, Integer distance) {
        this.lines = lines;
        this.distance = distance;
    }

    public Path(final List<Station> stations, final Set<Line> lines, final Integer distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public boolean isLongerThanTenAndLessThanFiftyKilometers() {
        return distance > DistanceFarePolicy.DISTANCE_THRESHOLD_AFTER_TEN.value()
                && distance <= DistanceFarePolicy.DISTANCE_THRESHOLD_AFTER_FIFTY.value();
    }

    public boolean isLongerThanFiftyKilometers() {
        return distance > DistanceFarePolicy.DISTANCE_THRESHOLD_AFTER_FIFTY.value();
    }

    public List<Station> getStations() {
        return stations;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public Integer getDistance() {
        return distance;
    }
}

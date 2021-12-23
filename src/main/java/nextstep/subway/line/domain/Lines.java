package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class Lines {
    private List<Line> lines;

    private Lines() {
    }

    private Lines(List<Line> lines) {
        this.lines = lines;
    }
    
    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Line> getLines() {
        return lines;
    }
    
    public Stations getAllStations() {
        List<Station> stations = new ArrayList<Station>();
        lines.stream()
        .forEach(line -> stations.addAll(line.getSections().getStations()));
        return Stations.from(stations);
    }
}

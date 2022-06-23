package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Lines {
    List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean hasStation(Station station) {
        return lines.stream().anyMatch(line -> line.getStations().contains(station));
    }

    public List<Line> getLines() {
        return lines;
    }
}

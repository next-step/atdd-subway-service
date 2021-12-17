package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

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
    
    public int calculatorMaxSurcharge(List<Station> stations) {
        return lines.stream()
                .mapToInt(line -> line.getSurcharge(stations))
                .max()
                .orElse(0);
    }
}

package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines() {
        lines = new ArrayList<>();
    }

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public List<Station> getStations() {
        return lines.stream()
                .flatMap(v -> v.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lines)) return false;
        Lines lines1 = (Lines) o;
        return Objects.equals(getLines(), lines1.getLines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLines());
    }

    @Override
    public String toString() {
        return "Lines{" +
                "lines=" + lines +
                '}';
    }
}

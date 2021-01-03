package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(Line... lines) {
        this.lines = Arrays.asList(lines);
    }

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Section> allSection() {
        return lines.stream()
                .flatMap(line -> line.getSection().stream())
                .collect(Collectors.toList());
    }
}


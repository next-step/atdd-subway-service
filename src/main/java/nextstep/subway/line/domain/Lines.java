package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Section> getSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}

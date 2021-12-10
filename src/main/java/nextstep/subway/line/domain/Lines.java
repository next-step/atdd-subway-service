package nextstep.subway.line.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Path toPath(Station source, Station target) {
        return Path.of(allSections(), source, target);
    }

    private Sections allSections() {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .collect(Collectors.collectingAndThen(Collectors.toList(), Sections::of));
    }
}

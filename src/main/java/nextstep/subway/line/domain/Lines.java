package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.Line.DEFAULT_LINE_SURCHARGE;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

public class Lines {
    public static final Lines EMPTY_LIST = Lines.from(Collections.emptyList());

    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Station> stations() {
        return lines.stream()
                .map(Line::sortStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Section> sections() {
        return lines.stream()
                .map(Line::sections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Fare maxLineSurcharge() {
        return Fare.from(lines.stream()
                .mapToInt(Line::lineSurcharge)
                .max()
                .orElse(DEFAULT_LINE_SURCHARGE));
    }

    public List<Line> list() {
        return lines;
    }
}

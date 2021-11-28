package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public final class Lines {

    private final List<Line> lines;

    private Lines(List<Line> lines) {
        Assert.notNull(lines, "지하철 노선 목록은 필수입니다.");
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public List<Station> stationList() {
        Stations stations = Stations.empty();
        for (Line line : lines) {
            stations = stations.merge(line.sortedStations());
        }
        return stations.list();
    }

    public List<Section> sectionList() {
        Sections sections = Sections.empty();
        for (Line line : lines) {
            sections = sections.merge(line.sections());
        }
        return sections.list();
    }

    public List<Line> list() {
        return Collections.unmodifiableList(lines);
    }

    public <R> List<R> mapToList(Function<Line, R> mapper) {
        return lines.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}

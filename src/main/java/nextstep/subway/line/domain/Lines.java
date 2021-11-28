package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public final class Lines {

    private final List<Line> list;

    private Lines(List<Line> list) {
        Assert.notNull(list, "지하철 노선 목록은 필수입니다.");
        this.list = list;
    }

    public static Lines from(List<Line> list) {
        return new Lines(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<Station> stationList() {
        Stations stations = Stations.empty();
        for (Line line : list) {
            stations = stations.merge(line.sortedStations());
        }
        return stations.list();
    }

    public List<Section> sectionList() {
        Sections sections = Sections.empty();
        for (Line line : list) {
            sections = sections.merge(line.sections());
        }
        return sections.list();
    }

    public List<Line> list() {
        return Collections.unmodifiableList(list);
    }

    public <R> List<R> mapToList(Function<Line, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}

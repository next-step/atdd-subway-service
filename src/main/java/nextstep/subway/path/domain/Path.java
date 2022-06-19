package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private List<Section> sections;
    private int distance;

    public Path(List<Section> sections, int distance) {
        this.sections = sections;
        this.distance = distance;
    }

    public static Path of(List<Section> sections, int distance) {
        return new Path(sections, distance);
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
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

    public int calculateDistanceFare() {
        FareDistancePolicyType policy = FareDistancePolicyType.of(distance);
        return policy.calculateFare(distance);
    }

    public int calculateLineFare() {
        List<Line> lines = findDistinctLines(sections);
        return lines.stream()
                .map(line -> line.getAdditionalFare().getValue())
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Line> findDistinctLines(List<Section> sections) {
        return sections.stream()
                .map(section -> section.getLine())
                .distinct()
                .collect(Collectors.toList());
    }
}

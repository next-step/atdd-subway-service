package nextstep.subway.path.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path valueOf(List<Station> stations, Distance distance) {
        return new Path(stations, distance);
    }

    public Set<Line> findPathLinesFrom(List<Section> sections) {
        return sections.stream()
                .filter(this::containSection)
                .map(Section::line)
                .collect(Collectors.toSet());
    }

    private boolean containSection(Section section) {
        Map<Station, Station> sections = new HashMap<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            sections.put(stations.get(i), stations.get(i + 1));
        }
        return isEqualUpStationAndDownStation(sections, section);
    }

    private boolean isEqualUpStationAndDownStation(Map<Station, Station> sections, Section section) {
        return isEqualUpStationAndDownStationForward(sections, section) || isEqualUpStationAndDownStationReverse(
                sections, section);
    }

    private boolean isEqualUpStationAndDownStationForward(Map<Station, Station> sections, Section section) {
        if (sections.containsKey(section.upStation())) {
            return isEqualDownStation(sections, section);
        }
        return false;
    }

    private boolean isEqualUpStationAndDownStationReverse(Map<Station, Station> sections, Section section) {
        if (sections.containsKey(section.downStation())) {
            return isEqualUpStation(sections, section);
        }
        return false;
    }

    private boolean isEqualDownStation(Map<Station, Station> sections, Section section) {
        return sections.get(section.upStation()).equals(section.downStation());
    }

    private boolean isEqualUpStation(Map<Station, Station> sections, Section section) {
        return sections.get(section.downStation()).equals(section.upStation());
    }

    public List<Station> stations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }

    public int distanceValue() {
        return distance.distance();
    }
}

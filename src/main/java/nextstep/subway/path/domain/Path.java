package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final Distance distance;
    private final Sections sections;

    private Path(List<Station> stations, Distance distance, Sections sections) {
        this.stations = stations;
        this.distance = distance;
        this.sections = sections;
    }

    public static Path of(List<Station> stations, int distance, List<Section> sections) {
        return new Path(
                stations,
                Distance.from(distance),
                Sections.of(sections)
        );
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Sections getSections() {
        return sections;
    }
}

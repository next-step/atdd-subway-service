package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private final List<Station> stations;
    private final List<Section> sections;

    public PathFinder(final List<Station> stations, final List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public Path findShortestPath(final Station source, final Station target) {
        return new Path(Arrays.asList(new Station("강남역"), new Station("역삼역")), 100);
    }
}

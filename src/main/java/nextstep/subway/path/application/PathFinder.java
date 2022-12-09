package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private final List<Section> allSections;

    private PathFinder(List<Section> allSections) {
        this.allSections = allSections;
    }

    public List<Station> findByDijkstra(Station departStation, Station destStation) {
        return null;
    }

    public List<Station> findByKShortest(Station departStation, Station destStation) {
        return null;
    }

    public static PathFinder of(List<Section> allSections) {
        return new PathFinder(allSections);
    }
}

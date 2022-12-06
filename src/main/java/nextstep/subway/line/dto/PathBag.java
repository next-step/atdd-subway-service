package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PathBag {
    private final List<SectionPath> sectionPaths;

    private PathBag(List<SectionPath> sectionPaths) {
        this.sectionPaths = sectionPaths;
    }

    public static PathBag from(List<SectionPath> sectionPaths) {
        return new PathBag(sectionPaths);
    }

    public static PathBag fromLines(List<Line> lines) {
        return new PathBag(lines.stream().map(Line::getSections)
                .flatMap(List::stream)
                .map(SectionPath::of)
                .collect(Collectors.toList()));
    }

    public List<Station> findVertex() {
        Set<Station> stationSet = new HashSet<>();
        sectionPaths.forEach(it -> {
            stationSet.add(it.getUpStation());
            stationSet.add(it.getDownStation());
        });
        return new ArrayList<>(stationSet);
    }

    public List<SectionPath> getSectionPaths() {
        return sectionPaths;
    }
}

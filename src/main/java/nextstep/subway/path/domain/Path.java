package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Path {
    private Sections sections;
    private int distance;

    public Path(Sections sections, int distance) {
        this.sections = sections;
        this.distance = distance;
    }

    public static Path of(List<SectionEdge> sectionEdges, int distance) {
        List<Section> sections = sectionEdges.stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
        return new Path(new Sections(sections), distance);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return DistanceFare.calculate(distance) + sections.getMaxSurcharge();
    }
}

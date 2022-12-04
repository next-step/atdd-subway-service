package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class Path {
    private final List<Long> stationIds;
    private final Distance distance;
    private final List<SectionEdge> sections;

    public Path(List<Long> stationIds, int distance, List<SectionEdge> sections) {
        this.stationIds = stationIds;
        this.distance = Distance.from(distance);
        this.sections = sections;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public int getAdditionalFareByLine() {
        return sections.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);
    }
}
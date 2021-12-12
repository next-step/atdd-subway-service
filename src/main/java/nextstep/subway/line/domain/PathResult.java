package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.line.infrastructure.path.SectionEdge;
import nextstep.subway.station.domain.Station;

public class PathResult {

    private final List<Station> result;
    private List<SectionEdge> sectionEdges;
    private final int weight;

    public PathResult(List<Station> result, List<SectionEdge> sectionEdges, int weight) {
        this.result = result;
        this.weight = weight;
        this.sectionEdges = sectionEdges;
    }

    public static PathResult of(List<Station> result, List<SectionEdge> sectionEdges, int weight) {
        return new PathResult(result, sectionEdges, weight);
    }

    public List<Station> getResult() {
        return result;
    }

    public int getWeight() {
        return weight;
    }

    public Money getMaxAdditionalFare() {
        return sectionEdges.stream()
            .map(SectionEdge::getAdditionalFare)
            .max(Money::compareTo)
            .orElse(Money.ZERO);
    }
}

package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private static final int BASIC_FARE = 1_250;

    private GraphPath graphPath;

    public Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> stations() {
        return graphPath.getVertexList();
    }

    public int distance() {
        return (int)graphPath.getWeight();
    }

    public int fare() {
        // TODO : 거리비례요금 합산, 할인정책 적용
        return BASIC_FARE + extraFare();
    }

    private int extraFare() {
        List<Integer> extraFares = sections().stream()
                                            .map(Section::getLine)
                                            .map(Line::getExtraFare)
                                            .collect(Collectors.toList());
        return Collections.max(extraFares);
    }

    private List<Section> sections() {
        return graphPath.getEdgeList();
    }
}

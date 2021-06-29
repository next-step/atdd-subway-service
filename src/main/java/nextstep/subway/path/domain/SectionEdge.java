package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {

    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLine(),
            section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public SectionEdge(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public SectionEdge(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLine(),
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance()
        );
    }

    public Line getLine() {
        return this.line;
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
                "line=" + line +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}

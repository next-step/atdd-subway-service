package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Line line;
    private Station upStation;
    private Station downStation;

    public SectionEdge(Line line, Station upStation, Station downStation) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLine(), section.getUpStation(), section.getDownStation());
    }

    public Line getLine() {
        return line;
    }
}

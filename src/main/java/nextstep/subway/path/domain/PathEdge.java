package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * packageName : nextstep.subway.path.domain
 * fileName : PathEdge
 * author : haedoang
 * date : 2021/12/12
 * description :
 */
public class PathEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Distance distance;
    private Line line;

    public PathEdge(Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.distance = section.getDistance();
        this.line = section.getLine();
    }

    public static PathEdge of(Section section) {
        return new PathEdge(section);
    }

    public Station sourceVertex() {
        return source;
    }

    public Station targetVertex() {
        return target;
    }

    public int weight() {
        return this.distance.intValue();
    }

    public Line line() {
        return line;
    }
}

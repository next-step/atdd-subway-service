package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathDto {
    private final Station source;
    private final Station target;
    private final Distance distance;

    private PathDto(Station source, Station target, Distance distance) {
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public static PathDto from(Section section) {
        return new PathDto(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }
}

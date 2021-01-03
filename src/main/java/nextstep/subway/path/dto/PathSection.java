package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Section;

public class PathSection {
    private PathStation source;
    private PathStation target;
    private int weight;

    public PathSection(PathStation source, PathStation target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public PathStation getSource() {
        return source;
    }

    public PathStation getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    public static PathSection of(Section section) {
        return new PathSection(PathStation.of(section.getUpStation()), PathStation.of(section.getDownStation()), section.getDistance());
    }


}

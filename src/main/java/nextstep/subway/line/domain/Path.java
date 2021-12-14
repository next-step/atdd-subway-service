package nextstep.subway.line.domain;

import java.util.Objects;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.line.application.PathSearch;
import nextstep.subway.line.application.exception.PathErrorCode;
import nextstep.subway.station.domain.Station;

public class Path {

    private PathSearch pathSearch;
    private final Sections sections;
    private final Station source;
    private final Station target;

    public Path(Sections sections, Station source, Station target, PathSearch pathSearch) {
        validNotSame(source, target);
        validContains(sections, source, target);

        this.sections = sections;
        this.source = source;
        this.target = target;
        this.pathSearch = pathSearch;
    }

    public Path(Sections sections, Station source, Station target) {
        validNotSame(source, target);
        validContains(sections, source, target);

        this.sections = sections;
        this.source = source;
        this.target = target;
    }

    public static Path of(Sections sections, Station source, Station target,
        PathSearch pathSearch) {
        return new Path(sections, source, target, pathSearch);
    }

    public static Path of(Sections sections, Station source, Station target) {
        return new Path(sections, source, target);
    }

    public Sections getSections() {
        return sections;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    private void validNotSame(Station source, Station target) {
        if (Objects.equals(source, target)) {
            throw InvalidParameterException.of(PathErrorCode.PATH_IN_OUT_SAME);
        }
    }

    private void validContains(Sections sections, Station source, Station target) {
        if (!sections.isContainStation(source) || !sections.isContainStation(target)) {
            throw InvalidParameterException.of(PathErrorCode.PATH_IN_OUT_NOT_FOUND);
        }
    }

    public PathResult getShortestPath() {
        return this.pathSearch.findShortestPath(this);
    }
}

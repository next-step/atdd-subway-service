package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

public class Path {

    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public static Path of(Sections sections) {
        return new Path(sections);
    }
}

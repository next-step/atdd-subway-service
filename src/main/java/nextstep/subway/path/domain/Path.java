package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public static Path of(Sections sections) {
        return new Path(sections);
    }

    public List<Station> findStations() {
        return this.sections.getStations();
    }

    public Distance findDistance() {
        return this.sections.findDistance();
    }
}

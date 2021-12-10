package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

/**
 * packageName : nextstep.subway.path.domain
 * fileName : Path
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
public class Path {
    private Sections sections;
    private Distance distance;

    public Path(Sections sections, Distance distance) {
        this.sections = sections;
        this.distance = distance;
    }

    public static Path of(Sections sections, Distance distance) {
        return new Path(sections, distance);
    }

    public Sections sections() {
        return sections;
    }

    public Distance distance() {
        return distance;
    }

    public List<Station> stations() {
        return sections.getStations();
    }

    public Station source() {
        return sections.firstStation();
    }

    public Station target() {
        return sections.lastStation();
    }
}

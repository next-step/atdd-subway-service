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
    private List<Station> stations;
    private Distance distance;

    public Path(Sections sections, List<Station> stations, Distance distance) {
        this.sections = sections;
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(Sections sections, List<Station> stations, Distance distance) {
        return new Path(sections, stations, distance);
    }

    public Sections sections() {
        return sections;
    }

    public Distance distance() {
        return distance;
    }

    public List<Station> stations() {
        return stations;
    }

    public Station source() {
        return sections.firstStation();
    }

    public Station target() {
        return sections.lastStation();
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Money;
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
    private List<PathEdge> sections;
    private List<Station> stations;
    private Station source;
    private Station target;
    private Distance distance;

    public Path(List<PathEdge> sections, List<Station> stations, Station source, Station target, Distance distance) {
        this.sections = sections;
        this.stations = stations;
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    public static Path of(List<PathEdge> edges, List<Station> stations, Station source, Station target, Distance distance) {
        return new Path(edges, stations, source, target, distance);
    }

    public List<PathEdge> sections() {
        return sections;
    }

    public Distance distance() {
        return distance;
    }

    public List<Station> stations() {
        return stations;
    }

    public Station source() {
        return source;
    }

    public Station target() {
        return target;
    }

    public Money extraCharge() {
        return Money.of(sections.stream()
                .map(it -> it.line().extraCharge())
                .max(Integer::compareTo)
                .orElse(Money.MIN_VALUE));
    }
}

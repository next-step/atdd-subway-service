package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationDuplicatedException;

import java.util.List;
import java.util.Objects;

/**
 * packageName : nextstep.subway.path.domain
 * fileName : Path
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
public class Path {
    private Station sourceStation;

    private Station targetStation;

    private List<Station> routes;

    private Distance distance;

    public Path(Station sourceStation, Station targetStation, List<Station> routes, Distance distance) {
        if (Objects.equals(sourceStation, targetStation)) {
            throw new StationDuplicatedException();
        }

        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.routes = routes;
        this.distance = distance;
    }

    public static Path of(Station sourceStation, Station targetStation, List<Station> stations, Distance distance) {
        return new Path(sourceStation, targetStation, stations, distance);
    }

    public Station source() {
        return sourceStation;
    }

    public Station target() {
        return targetStation;
    }

    public List<Station> routes() {
        return routes;
    }

    public Distance distance() {
        return distance;
    }
}

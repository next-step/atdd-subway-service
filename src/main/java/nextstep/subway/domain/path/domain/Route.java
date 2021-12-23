package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Section;
import nextstep.subway.domain.path.exception.NotConnectedStation;
import nextstep.subway.domain.station.domain.Station;

import java.util.List;

public class Route {

    private static final int PATH_NODE_MINIMUM_NUMBER = 2;
    private final List<Station> stations;
    private final List<Section> sections;
    private final Distance distance;

    public Route(List<Station> stations, List<Section> sections, Distance distance) {
        pathNodeMinimumNumberValidation(stations);
        this.stations = stations;
        this.sections = sections;
        this.distance = distance;
    }

    private void pathNodeMinimumNumberValidation(List<Station> stations) {
        if (stations.size() < PATH_NODE_MINIMUM_NUMBER) {
            throw new NotConnectedStation();
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }
}

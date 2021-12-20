package nextstep.subway.path.domain;

import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class Path {
    private List<Station> stations;
    private Distance distance;
    private Sections sections;

    private Path(final Distance distance, final Sections sections, final List<Station> stations) {
        validateConnectedStations(stations);
        this.distance = distance;
        this.stations = stations;
        this.sections = sections;
    }

    public Path(final Distance distance) {
        this.distance = distance;
    }

    public static Path of(final int weight, List<Section> sections, final List<Station> stations) {
        return new Path(Distance.of(weight), Sections.from(sections), stations);
    }

    private void validateConnectedStations(final List<Station> stations) {
        if (CollectionUtils.isEmpty(stations)) {
            throw new PathDisconnectedException();
        }
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare mergeFare(final Fare distance) {
        return distance.plus(getMaxExtraFare());
    }

    public Fare getMaxExtraFare() {
        return this.sections.getMaxExtraFare();
    }

    public List<Station> getStations() {
        return this.stations;
    }
}

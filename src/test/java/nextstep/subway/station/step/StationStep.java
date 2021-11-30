package nextstep.subway.station.step;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;

public final class StationStep {

    public static Station station(String name) {
        return Station.from(Name.from(name));
    }

}

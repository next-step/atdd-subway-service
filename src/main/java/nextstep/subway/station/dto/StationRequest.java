package nextstep.subway.station.dto;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;

public class StationRequest {
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Name name() {
        return Name.from(name);
    }

    public Station toStation() {
        return Station.from(Name.from(name));
    }
}

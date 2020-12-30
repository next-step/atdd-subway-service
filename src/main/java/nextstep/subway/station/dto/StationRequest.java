package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

import javax.validation.constraints.NotNull;

public class StationRequest {
    @NotNull(message = "역 이름은 필수값입니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}

package nextstep.subway.station.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationRequest {
    @NotBlank
    private String name;

    public Station toStation() {
        return new Station(name);
    }
}

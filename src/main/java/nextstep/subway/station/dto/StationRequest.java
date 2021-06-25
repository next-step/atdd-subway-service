package nextstep.subway.station.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseRequest;
import nextstep.subway.station.domain.Station;

@Getter
@NoArgsConstructor
public class StationRequest extends BaseRequest<Station> {
    private String name;

    @Builder
    public StationRequest(String name) {
        this.name = name;
    }

    @Override
    public Station toEntity() {
        return new Station(name);
    }
}

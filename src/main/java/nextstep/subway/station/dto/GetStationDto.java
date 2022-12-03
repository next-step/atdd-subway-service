package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

public class GetStationDto {

    private final Station sourceStation;
    private final Station targetStation;

    public GetStationDto(Station sourceStation, Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static GetStationDto of(Station sourceStation, Station targetStation) {
        return new GetStationDto(sourceStation, targetStation);
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}

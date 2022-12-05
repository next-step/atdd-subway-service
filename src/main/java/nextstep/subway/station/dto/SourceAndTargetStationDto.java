package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

public class SourceAndTargetStationDto {

    private final Station sourceStation;
    private final Station targetStation;

    public SourceAndTargetStationDto(Station sourceStation, Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static SourceAndTargetStationDto of(Station sourceStation, Station targetStation) {
        return new SourceAndTargetStationDto(sourceStation, targetStation);
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}

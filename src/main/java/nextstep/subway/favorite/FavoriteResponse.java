package nextstep.subway.favorite;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long Id;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    public Long getId() {
        return Id;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }
}

package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long memberId, StationResponse sourceStation, StationResponse targetStation) {
        this.id = memberId;
        this.source = sourceStation;
        this.target = targetStation;
    }

    public static FavoriteResponse of(Long memberId, StationResponse stationResponse, StationResponse targetStation) {
        return new FavoriteResponse(memberId, stationResponse, targetStation);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}

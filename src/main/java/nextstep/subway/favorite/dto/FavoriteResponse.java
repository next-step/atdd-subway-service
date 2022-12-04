package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private String id;
    StationResponse source;
    StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(String id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }
}

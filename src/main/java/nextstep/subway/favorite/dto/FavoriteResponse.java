package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(){

    }

    public Long getId() {
        return id;
    }
}

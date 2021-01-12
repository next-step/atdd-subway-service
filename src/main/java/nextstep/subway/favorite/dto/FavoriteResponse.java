package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }
}

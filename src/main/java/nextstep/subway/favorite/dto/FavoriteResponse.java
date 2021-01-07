package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private final long id;
    private final StationResponse source;
    private final StationResponse target;

    private FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(long id, Station source, Station target) {
        return new FavoriteResponse(id, StationResponse.of(source), StationResponse.of(target));
    }

    public static FavoriteResponse of(long id, StationResponse source, StationResponse target) {
        return new FavoriteResponse(id, source, target);
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}

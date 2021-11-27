package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    private FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.id(),
            StationResponse.from(favorite.source()),
            StationResponse.from(favorite.target()));
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
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

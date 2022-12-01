package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = StationResponse.of(favorite.getSourceStation());
        this.target = StationResponse.of(favorite.getTargetStation());
    }

    public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
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

package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse sourceStation, StationResponse targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static FavoriteResponse of(Favorite favorite) {
        StationResponse sourceStation = StationResponse.of(favorite.getSourceStation());
        StationResponse targetStation = StationResponse.of(favorite.getTargetStation());
        return new FavoriteResponse(favorite.getId(), sourceStation, targetStation);
    }

    public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }
}
